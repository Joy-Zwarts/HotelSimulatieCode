public class OpstartScherm {
    private String gekozenFile;
    private ExterneLibrary gekozenEventLibrary;
    private OpstartScherm opstartScherm;

    public OpstartScherm(String gekozenFile, ExterneLibrary gekozenEventLibrary, OpstartScherm opstartScherm) {
        this.gekozenFile = gekozenFile;
        this.gekozenEventLibrary = gekozenEventLibrary;
        this.opstartScherm = opstartScherm;
    }

    public void startSimulatie() {
    }

    public String vraagJsonBestand() {
        return this.gekozenFile;
    }

    public ExterneLibrary vraagEventLibrary() {
        return this.gekozenEventLibrary;
    }

    public String getGekozenFile() {
        return this.gekozenFile;
    }

    public void setGekozenFile(String gekozenFile) {
        this.gekozenFile = gekozenFile;
    }

    public ExterneLibrary getGekozenEventLibrary() {
        return this.gekozenEventLibrary;
    }

    public void setGekozenEventLibrary(ExterneLibrary gekozenEventLibrary) {
        this.gekozenEventLibrary = gekozenEventLibrary;
    }
}
