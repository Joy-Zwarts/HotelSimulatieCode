package Controller.GastManagement;

import Model.Ruimtes.RuimteModel;

// notified listeners als een nieuwe kamer is aangemaakt
public interface NewRoom {
    void onNewRoom(RuimteModel kamer);
}
