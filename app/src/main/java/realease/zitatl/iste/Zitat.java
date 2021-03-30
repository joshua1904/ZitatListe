package realease.zitatl.iste;

public class Zitat {
    String zitat;
    String datum;
    String name;

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public boolean isBier() {
        return bier;
    }

    public void setBier(boolean bier) {
        this.bier = bier;
    }

    public boolean isWeed() {
        return weed;
    }

    public void setWeed(boolean weed) {
        this.weed = weed;
    }

    boolean fav, bier,weed,isFake= false;
    public Zitat(String zitatNeu,String datumNeu,String nameNeu,boolean weedNeu,boolean bierNeu,boolean favNeu)
    {
        zitat = zitatNeu;
        datum = datumNeu;
        name = nameNeu;
        bier = bierNeu;
        weed = weedNeu;
        fav = favNeu;
    }
    /**
     * erstellt nur einen String und kein ganzes Zitat
     */
    public Zitat(String fake)
    {
        zitat = fake;
        datum = "";
        name = "";
        bier = false;
        weed = false;
        fav = false;
        isFake = true;
    }

    public String getZitat() {
        return zitat;
    }

    public void setZitat(String zitat) {
        this.zitat = zitat;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String ganzesZitatGeben()
    {
        if(!isFake) {
            String zitate = " \"" + zitat + "\"" + " \n" + datum + " ~" + name + "~";

            return zitate;
        }else return fakeZitatGeben();
    }
    public String fakeZitatGeben()
    {
        return zitat;
    }
    public boolean weedIntus()
    {
        return weed;
    }
    public boolean bierIntus()
    {
        return bier;
    }
    public boolean fav()
    {
        return fav;
    }


}
