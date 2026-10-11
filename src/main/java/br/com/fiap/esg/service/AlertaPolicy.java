package br.com.fiap.esg.service;

/** Regra de negocio: classifica severidade pelo percentual acima do limite contratado. */
public final class AlertaPolicy {

    private AlertaPolicy() {}

    public static String classificar(double consumoKwh, double limiteKwh) {
        if (limiteKwh <= 0 || consumoKwh <= limiteKwh) {
            return "NORMAL";
        }
        double excessoPct = (consumoKwh - limiteKwh) / limiteKwh * 100.0;
        if (excessoPct >= 25.0) return "CRITICA";
        if (excessoPct >= 10.0) return "ALTA";
        return "MEDIA";
    }
}
