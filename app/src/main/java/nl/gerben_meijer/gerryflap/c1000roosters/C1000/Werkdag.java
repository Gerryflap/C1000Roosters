package nl.gerben_meijer.gerryflap.c1000roosters.C1000;

/**
 * Created by Gerryflap on 2015-05-31.
 */
public class Werkdag {
    private String datum;
    private String start;
    private String eind;
    private String pauze;
    private String dag;
    private String totaal;

    public Werkdag(String dag, String datum, String start, String eind, String pauze, String totaal) {
        this.datum = datum;
        this.dag = dag;
        this.start = start;
        this.eind = eind;
        this.pauze = pauze;
        this.totaal = totaal;
    }

    public String getStart() {
        return start;
    }

    public String getEind() {
        return eind;
    }

    public String getPauze() {
        return pauze;
    }

    public String getDag() {
        return dag;
    }

    public String getDatum() {
        return datum;
    }

    public String getTotaal() {
        return totaal;
    }

    public String toString(){
        return String.format("%s%s \nstart: %s eind: %s pauze: %s", dag, datum, start, eind, pauze);
    }


}
