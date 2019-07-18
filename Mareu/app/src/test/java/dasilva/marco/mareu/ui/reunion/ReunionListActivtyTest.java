package dasilva.marco.mareu.ui.reunion;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import dasilva.marco.mareu.di.DI;
import dasilva.marco.mareu.model.Reunion;
import dasilva.marco.mareu.service.ReunionApiService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)


public class ReunionListActivtyTest {

   ReunionApiService service;
   Reunion reunion;
   List<Reunion> reunionList;

    @Before
    public void setUp(){
        service = DI.getReunionApiService();
        reunionList = service.getReunions();
        reunion = new Reunion(Reunion.getRandomColorAvatar(), "24/10/2019",
                "10h:30m", "Paris", "Téléphone", "Mario, Luigi, Bowser");
        service.addReunion(reunion);
    }
    @Test
    public void addingReunionWithSucess(){
        Reunion customReunion = new Reunion(Reunion.getRandomColorAvatar(), "02/09/2019",
                "10h:30m", "Paris", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion);
        assertTrue(service.getReunions().get(1).getDate().contains(customReunion.getDate()));
    }
    @Test
    public void sortItemsInRecycleViewByPlaceWithSuccess(){
        Reunion customReunion = new Reunion(Reunion.getRandomColorAvatar(), "02/09/2019",
                "10h:30m", "Salle 2", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion);
        reunionList =  service.setListReunionByPlace("Salle 2");
        assertTrue(reunionList.contains(customReunion));

    }
    @Test
    public void sortItemsInRecyclerViewByDateWithSuccess(){
        Reunion customReunion = new Reunion(Reunion.getRandomColorAvatar(), "02/09/2031",
                "10h:30m", "Paris", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion);
        reunionList = service.setListReunionByDate("02/09/2031");
        assertTrue(reunionList.contains(customReunion));
    }
    @Test
    public void whenDeletingReunion_itemIsNotDisplayedAnymore(){
        service.deleteReunion(reunion);
        assertFalse(service.getReunions().contains(reunion));
    }
}
