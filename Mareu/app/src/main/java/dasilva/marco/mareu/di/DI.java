package dasilva.marco.mareu.di;

import dasilva.marco.mareu.service.FakeReunionApiService;
import dasilva.marco.mareu.service.ReunionApiService;

import dasilva.marco.mareu.service.FakeReunionApiService;
import dasilva.marco.mareu.service.ReunionApiService;

public class DI {

    private static ReunionApiService service = new FakeReunionApiService();

    public static ReunionApiService getReunionApiService(){
        return service;
    }

}
