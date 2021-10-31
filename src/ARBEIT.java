import javakarol.Roboter;
import javakarol.Welt;

public class ARBEIT {
    private final Welt welt;
    private final Roboter karol;
    public ARBEIT() {
        this(10, 10, 10, 1, 1, 'S');
    }
    public ARBEIT(int breite, int laenge, int hoehe) {
        this(breite, laenge, hoehe, 1, 1, 'S');
    }
    public ARBEIT(int breite, int laenge, int hoehe, int startX, int startY, char startBlickrichtung) {
        welt = new Welt(breite, laenge, hoehe);
        karol = new Roboter(startX, startY, startBlickrichtung, welt);
    }
    public ARBEIT(int startX, int startY, char startBlickrichtung) {
        this(10, 10, 10, startX, startY, startBlickrichtung);
    }
    public void ViererreiheAusfuehren(int x, int y) {
        Ausfuehren(Commandos.Viererreihe, x, y, 'S');
        while (karol.BlickrichtungGeben()!='S') karol.LinksDrehen();
    }
    public void QuadratAusfuehren(int x, int y) {
        Ausfuehren(Commandos.Quadrat, x, y, 'S');
        while (karol.BlickrichtungGeben()!='S') karol.LinksDrehen();
    }
    public void Ausfuehren(Commandos commando, int x, int y, char Richtung) {
        ZuPositionBewegen(x, y, Richtung);
        while (karol.BlickrichtungGeben()!=Richtung) karol.LinksDrehen();
        switch (commando) {
            case Quadrat:
                for (int i = 0; i < 6; i++) {
                    if (i == 2 || i == 4) {
                        karol.LinksDrehen();
                        continue;
                    }
                    karol.Hinlegen();
                    karol.Schritt();
                }
                XmalAusfuehren(() -> {karol.Schritt();karol.LinksDrehen();}, 2);
                break;
            case Viererreihe:
                for (int i = 0; i < 4; i++) {
                    karol.Schritt();
                    karol.Hinlegen();
                }
                XmalAusfuehren(karol::Schritt, 2);
                XmalAusfuehren(karol::LinksDrehen, 2);
                XmalAusfuehren(karol::Schritt, 5);
                break;
            default: break;
        }
    }
    public void XmalAusfuehren(Runnable runnable, int anzahl) {
        for (int i = 0; i < anzahl; i++) {
            runnable.run();
        }
    }
    public void ZuPositionBewegen(int x, int y, char Blickrichtung) {
        if (!gebenObGueltigeRichtung(Blickrichtung)) return;
        while (karol.PositionYGeben()!=y) {
            while (karol.AnzahlZiegelVorneGeben()>karol.SprungshoeheGeben()) karol.Aufheben();
            if (karol.PositionYGeben()>y) {
                while (karol.BlickrichtungGeben()!='N') karol.LinksDrehen();
                karol.Schritt();
            }else if (karol.PositionYGeben()<y) {
                while (karol.BlickrichtungGeben()!='S') karol.LinksDrehen();
                karol.Schritt();
            }
        }
        while (karol.PositionXGeben()!=x) {
            while (karol.AnzahlZiegelVorneGeben()>karol.SprungshoeheGeben()) karol.Aufheben();
            if (karol.PositionXGeben()>x) {
                while (karol.BlickrichtungGeben()!='W') karol.LinksDrehen();
                karol.Schritt();
            }else if (karol.PositionXGeben()<x) {
                while (karol.BlickrichtungGeben()!='O') karol.LinksDrehen();
                karol.Schritt();
            }
        }
        while (karol.BlickrichtungGeben()!=Blickrichtung) karol.LinksDrehen();
    }
    private boolean gebenObGueltigeRichtung(char richtung) {
        return richtung=='N'||richtung=='O'||richtung=='S'||richtung=='W';
    }
    public enum Commandos {
        Quadrat,
        Viererreihe,
        Aufgabe2; //Keine Ahnung was diese Aufgabe ist
    }
}
