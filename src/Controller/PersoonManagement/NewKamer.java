package Controller.PersoonManagement;

import Model.Ruimtes.RuimteModel;

// notified listeners als een nieuwe kamer is aangemaakt
public interface NewKamer {
    void onNewKamer(RuimteModel kamer);
}
