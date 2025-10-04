package com.example.laba1koz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GraphView extends View {

    private List<Float> dataPoints;
    private final String LOG_TAG = "GRAPH_ACTIVITY";
    private float timePeriod;
    private List<Float> spectrumPoints;
    private boolean showSpectrum = false;
    private static final float MAX_DISPLAY_FREQUENCY = 50000.0f;
    private int disFreq;


    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // ОБЯЗАТЕЛЬНО вызвать setMeasuredDimension!
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        if (dataPoints != null && !dataPoints.isEmpty()) {
            drawGridAndAxes(canvas);

            if (showSpectrum) {
                drawSpectrum(canvas);
            } else {
                drawSineWave(canvas);
            }

            drawDebugInfo(canvas);
        } else {
            drawNoDataMessage(canvas);
        }
    }

    private void drawSpectrum(Canvas canvas) {
        if (spectrumPoints == null || spectrumPoints.size() < 2) return;

        Paint linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(2f);
        linePaint.setStyle(Paint.Style.STROKE);

        Paint fillPaint = new Paint();
        fillPaint.setColor(Color.argb(100, 255, 0, 0));
        fillPaint.setStyle(Paint.Style.FILL);

        int width = getWidth();
        int height = getHeight();
        float padding = 50f;

        float graphWidth = width - 2 * padding;
        float graphHeight = height - 2 * padding;

        float maxVal = getMaxSpectrumValue();
        float minVal = getMinSpectrumValue();
        float valueRange = maxVal - minVal;

        if (valueRange == 0) valueRange = 1;

        Log.d(LOG_TAG, String.format("Спектр: min=%.3f, max=%.3f, точек=%d",
                minVal, maxVal, spectrumPoints.size()));

        float barWidth = graphWidth / spectrumPoints.size();
        if (barWidth < 1f) barWidth = 1f;

        for (int i = 0; i < spectrumPoints.size(); i++) {
            float x = padding + i * barWidth;
            float barHeight = ((spectrumPoints.get(i) - minVal) / valueRange) * graphHeight;
            float y = height - padding - barHeight;

            if (barHeight > 0) {
                canvas.drawRect(x, y, x + barWidth * 0.9f, height - padding, fillPaint);
                canvas.drawRect(x, y, x + barWidth * 0.9f, height - padding, linePaint);
            }
        }
    }

    private void drawNoDataMessage(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(50);
        canvas.drawText("Нет данных для графика", 50, 100, textPaint);
    }

    private void drawGridAndAxes(Canvas canvas) {
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(2f);

        int width = getWidth();
        int height = getHeight();
        float padding = 50f;

        for (int i = 0; i <= 5; i++) {
            float y = padding + ((height - 2 * padding) / 5) * i;
            canvas.drawLine(padding, y, width - padding, y, gridPaint);
        }

        for (int i = 0; i <= 5; i++) {
            float x = padding + ((width - 2 * padding) / 5) * i;
            canvas.drawLine(x, padding, x, height - padding, gridPaint);
        }

        Paint axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(3f);

        float centerY = height / 2;
        canvas.drawLine(padding, centerY, width - padding, centerY, axisPaint);
    }

    private void drawSineWave(Canvas canvas) {
        if (dataPoints.size() < 2) return;

        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(4f);
        linePaint.setStyle(Paint.Style.STROKE);

        int width = getWidth();
        int height = getHeight();
        float padding = 50f;

        //Log.d(LOG_TAG, "drawSineWave: getted width nd");

        float graphWidth = width - 2 * padding;
        float graphHeight = height - 2 * padding;

        float maxVal = getMaxValue();
        float minVal = getMinValue();
        float valueRange = maxVal - minVal;

        if (valueRange == 0) valueRange = 1;

        for (int i = 0; i < dataPoints.size() - 1; i++) {
            float x1 = padding + (graphWidth / (dataPoints.size() - 1)) * i;
            float y1 = height - padding - ((dataPoints.get(i) - minVal) / valueRange * graphHeight);

            float x2 = padding + (graphWidth / (dataPoints.size() - 1)) * (i + 1);
            float y2 = height - padding - ((dataPoints.get(i + 1) - minVal) / valueRange * graphHeight);

            canvas.drawLine(x1, y1, x2, y2, linePaint);
        }
    }

    private void drawDebugInfo(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);

        if (showSpectrum) {
            canvas.drawText("СПЕКТР", 50, 80, textPaint);
            canvas.drawText("Точек: " + spectrumPoints.size(), 50, 130, textPaint);
            canvas.drawText("Min: " + String.format("%.2f", getMinSpectrumValue()), 50, 180, textPaint);
            canvas.drawText("Max: " + String.format("%.2f", getMaxSpectrumValue()), 50, 230, textPaint);

            if (spectrumPoints != null && !spectrumPoints.isEmpty()) {
                float freqResolution = (float) disFreq / (FFT.nextPowerOf2(dataPoints.size()) * 2);
                float maxDisplayedFreq = spectrumPoints.size() * freqResolution;
                canvas.drawText("Диапазон: 0-" + String.format("%.0f Гц", maxDisplayedFreq), 50, 280, textPaint);

                int peakFreqIndex = findPeakFrequencyIndex();
                if (peakFreqIndex != -1) {
                    float peakFreq = peakFreqIndex * freqResolution;
                    float peakAmplitude = spectrumPoints.get(peakFreqIndex);
                    canvas.drawText("Пик: " + String.format("%.1f Гц", peakFreq), 50, 330, textPaint);
                    canvas.drawText("Амплитуда: " + String.format("%.3f", peakAmplitude), 50, 380, textPaint);
                }
            }
        } else {
            canvas.drawText("СИГНАЛ", 50, 80, textPaint);
            canvas.drawText("Точек: " + dataPoints.size(), 50, 130, textPaint);
            canvas.drawText("Min: " + String.format("%.2f", getMinValue()), 50, 180, textPaint);
            canvas.drawText("Max: " + String.format("%.2f", getMaxValue()), 50, 230, textPaint);

            if (dataPoints != null && !dataPoints.isEmpty()) {
                canvas.drawText("Периодов: 3", 50, 280, textPaint);
                canvas.drawText("Время: " + String.format("%.3f сек", timePeriod), 50, 330, textPaint);

                float actualFreq = 3.0f / timePeriod;
                canvas.drawText("Частота: " + String.format("%.1f Гц", actualFreq), 50, 380, textPaint);
            }
        }
    }

    public void setData(int amp, int freq, int phase, int disFreq, int dotNum, float timePeriod) {
        this.timePeriod = timePeriod;
        this.disFreq = disFreq;
        dataPoints = new ArrayList<>();

        Log.d(LOG_TAG, String.format("setData: amp=%d, freq=%d, phase=%d, disFreq=%d, dots=%d, time=%.3f",
                amp, freq, phase, disFreq, dotNum, timePeriod));

        for (int i = 0; i < dotNum; i++) {
            float time = (float) i / disFreq;

            if (freq != 0) {
                float y = amp * (float) Math.sin(2 * Math.PI * freq * time + phase);
                dataPoints.add(y);
            } else {
                float y = (float) amp;
                dataPoints.add(y);
            }
        }

        if (!dataPoints.isEmpty()) {
            StringBuilder sb = new StringBuilder("Первые 5 точек: ");
            for (int i = 0; i < Math.min(5, dataPoints.size()); i++) {
                sb.append(String.format("%.2f", dataPoints.get(i))).append(" ");
            }
            Log.d(LOG_TAG, sb.toString());

            Log.d(LOG_TAG, String.format("Реальные min=%.2f, max=%.2f", getMinValue(), getMaxValue()));
        }

        calculateSpectrum();

        Log.d(LOG_TAG, String.format("График: %.3fсек, %d точек, freq=%dГц", timePeriod, dotNum, freq));
        invalidate();
    }

    private void calculateSpectrum() {
        if (dataPoints == null || dataPoints.isEmpty()) return;

        int n = dataPoints.size();
        int fftSize = FFT.nextPowerOf2(n);

        Log.d(LOG_TAG, "Размер БПФ: " + fftSize + " (исходный: " + n + ")");

        Complex[] signal = new Complex[fftSize];
        for (int i = 0; i < fftSize; i++) {
            if (i < n) {
                signal[i] = new Complex(dataPoints.get(i), 0);
            } else {
                signal[i] = new Complex(0, 0);
            }
        }

        try {
            Complex[] spectrum = FFT.fft(signal);

            spectrumPoints = new ArrayList<>();

            float frequencyResolution = (float) disFreq / fftSize;

            float maxPossibleFrequency = (float) disFreq / 2;

            float displayLimit = Math.min(MAX_DISPLAY_FREQUENCY, maxPossibleFrequency);
            int maxDisplayIndex = Math.min(spectrum.length / 2,
                    (int)(displayLimit / frequencyResolution));

            Log.d(LOG_TAG, String.format("Частотное разрешение: %.2f Гц", frequencyResolution));
            Log.d(LOG_TAG, String.format("Макс. возможная частота: %.1f Гц", maxPossibleFrequency));
            Log.d(LOG_TAG, String.format("Показываем до %d точек (%.1f Гц)",
                    maxDisplayIndex, maxDisplayIndex * frequencyResolution));

            for (int i = 0; i < maxDisplayIndex; i++) {
                if (spectrum[i] != null) {
                    float amplitude = spectrum[i].abs() / fftSize;
                    spectrumPoints.add(amplitude);
                } else {
                    spectrumPoints.add(0f);
                }
            }

            Log.d(LOG_TAG, "Рассчитан спектр: " + spectrumPoints.size() + " точек (ограничено до " +
                    displayLimit + " Гц)");

            if (!spectrumPoints.isEmpty()) {
                StringBuilder sb = new StringBuilder("Первые 10 значений спектра: ");
                for (int i = 0; i < Math.min(10, spectrumPoints.size()); i++) {
                    sb.append(String.format("%.3f", spectrumPoints.get(i))).append(" ");
                }
                Log.d(LOG_TAG, sb.toString());
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Ошибка расчета БПФ: " + e.getMessage());
            e.printStackTrace();
            spectrumPoints = new ArrayList<>();
        }
    }

    private float getMaxValue() {
        float max = Float.MIN_VALUE;
        for (float value : dataPoints) {
            if (value > max) max = value;
        }
        return max;
    }

    private float getMinValue() {
        float min = Float.MAX_VALUE;
        for (float value : dataPoints) {
            if (value < min) min = value;
        }
        return min;
    }

    private float getMaxSpectrumValue() {
        if (spectrumPoints == null) return 1;
        float max = Float.MIN_VALUE;
        for (float value : spectrumPoints) {
            if (value > max) max = value;
        }
        return max;
    }

    private float getMinSpectrumValue() {
        if (spectrumPoints == null) return 0;
        float min = Float.MAX_VALUE;
        for (float value : spectrumPoints) {
            if (value < min) min = value;
        }
        return min;
    }

    public void toggleView() {
        showSpectrum = !showSpectrum;
        invalidate();
    }

    private int findPeakFrequencyIndex() {
        if (spectrumPoints == null || spectrumPoints.isEmpty()) return -1;

        int peakIndex = 0;
        float peakValue = spectrumPoints.get(0);

        for (int i = 1; i < spectrumPoints.size(); i++) {
            if (spectrumPoints.get(i) > peakValue) {
                peakValue = spectrumPoints.get(i);
                peakIndex = i;
            }
        }

        return peakIndex;
    }

}
