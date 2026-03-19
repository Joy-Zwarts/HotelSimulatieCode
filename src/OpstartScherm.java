public class OpstartScherm {
    private String gekozenFile;
    private OpstartScherm opstartScherm;

    public OpstartScherm(String gekozenFile, OpstartScherm opstartScherm) {
        this.gekozenFile = gekozenFile;
        this.opstartScherm = opstartScherm;
    }

    public void startSimulatie() {
    }

    public String vraagJsonBestand() {
        return this.gekozenFile;
    }

    public String getGekozenFile() {
        return this.gekozenFile;
    }

    public void setGekozenFile(String gekozenFile) {
        this.gekozenFile = gekozenFile;
    }
}
