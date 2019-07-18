package dasilva.marco.mareu.events;

import dasilva.marco.mareu.model.Reunion;

public class DeleteReunionEvent {
    public Reunion reunion;

    public DeleteReunionEvent(Reunion reunion){
        this.reunion = reunion;
    }
}
