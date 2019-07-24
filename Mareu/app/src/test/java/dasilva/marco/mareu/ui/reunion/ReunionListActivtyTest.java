package dasilva.marco.mareu.ui.reunion;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import dasilva.marco.mareu.di.DI;
import dasilva.marco.mareu.model.Reunion;
import dasilva.marco.mareu.service.ReunionApiService;

import static org.junit.Assert.assertEquals;
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
        List<Reunion> reunionByDate = new ArrayList<>();
        Reunion customReunion = new Reunion(Reunion.getRandomColorAvatar(), "02/09/2031",
                "10h:30m", "Paris", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion);
        reunionByDate = service.setListReunionByDate("02/09/2031");
        assertTrue(reunionByDate.contains(customReunion));
    }
    @Test
    public void whenDeletingReunion_itemIsNotDisplayedAnymore(){
        service.deleteReunion(reunion);
        assertFalse(service.getReunions().contains(reunion));
    }
    @Test
    public void checkMaxReunions(){
        List<Reunion> reunions = service.getReunions();
        Reunion customReunion = new Reunion(Reunion.getRandomColorAvatar(), "02/09/2031",
                "10h:30m", "Salle 1", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion);
        Reunion customReunion1 = new Reunion(Reunion.getRandomColorAvatar(), "10/09/2031",
                "10h:30m", "Salle 1", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion1);
        Reunion customReunion2 = new Reunion(Reunion.getRandomColorAvatar(), "09/09/2031",
                "10h:30m", "Salle 1", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion2);
        Reunion customReunion3 = new Reunion(Reunion.getRandomColorAvatar(), "08/09/2031",
                "10h:30m", "Salle 1", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion3);
        Reunion customReunion4 = new Reunion(Reunion.getRandomColorAvatar(), "07/09/2031",
                "10h:30m", "Salle 1", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion4);
        Reunion customReunion5 = new Reunion(Reunion.getRandomColorAvatar(), "06/09/2031",
                "10h:30m", "Salle 1", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion5);
        Reunion customReunion6 = new Reunion(Reunion.getRandomColorAvatar(), "05/09/2031",
                "10h:30m", "Salle 1", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion6);
        Reunion customReunion7 = new Reunion(Reunion.getRandomColorAvatar(), "04/09/2031",
                "10h:30m", "Salle 1", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion7);
        Reunion customReunion8 = new Reunion(Reunion.getRandomColorAvatar(), "03/09/2031",
                "10h:30m", "Salle 1", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion8);
        Reunion customReunion9 = new Reunion(Reunion.getRandomColorAvatar(), "01/09/2031",
                "10h:30m", "Salle 1", "Jeu Vidéo", "Mario, Luigi, Bowser");
        service.addReunion(customReunion9);
        Reunion customReunion10 = new Reunion(Reunion.getRandomColorAvatar(), "11/09/2031",
                "10h:30m", "Salle 1", "Jeu Vidéo", "Mario, Luigi, Bowser");
        int count = 0;
        for (Reunion reunion : reunions){
            if (reunion.getPlace().contains("Salle 1")){
                count++;
            }
        }

        if (count < 10) {
            service.addReunion(customReunion10);
        }

        assertEquals(count, 10);
        assertFalse(service.getReunions().contains(customReunion10));

    }
}
