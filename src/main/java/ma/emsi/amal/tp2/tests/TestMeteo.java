package ma.emsi.amal.tp2.tests;

import ma.emsi.amal.tp2.tests.outil.meteo.MeteoTool;

public class TestMeteo {

    public static void main(String[] args) {

        MeteoTool meteoTool = new MeteoTool();

        boolean pluie = meteoTool.isPluiePrevue(
                48.85,
                2.35,
                0,
                3
        );

        System.out.println("Pluie prévue à Paris : " + pluie);
    }
}
