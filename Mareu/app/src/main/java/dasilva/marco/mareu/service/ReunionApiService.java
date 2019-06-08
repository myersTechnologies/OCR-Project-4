package dasilva.marco.mareu.service;

import dasilva.marco.mareu.model.Reunion;

import java.util.List;

import dasilva.marco.mareu.model.Reunion;

public interface ReunionApiService {

    List<Reunion> getReunions();

    void addReunion(Reunion reunion);

    void deleteReunion(Reunion reunion);

    void setListReunionByDate();

    void setListReunionByPlace();

}
