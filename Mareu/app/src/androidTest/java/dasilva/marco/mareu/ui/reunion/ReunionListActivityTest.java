package dasilva.marco.mareu.ui.reunion;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import dasilva.marco.mareu.R;
import dasilva.marco.mareu.di.DI;
import dasilva.marco.mareu.model.Reunion;
import dasilva.marco.mareu.service.ReunionApiService;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ReunionListActivityTest {

    private ReunionApiService service;
    private Reunion reunion;

    @Rule
    public ActivityTestRule<ReunionListActivity> mActivityTestRule = new ActivityTestRule<>(ReunionListActivity.class);

    @Before
    public void setUp(){
        service = DI.getReunionApiService();
        reunion = new Reunion(Reunion.getRandomColorAvatar(), "24/10/2019",
                "10h:30m", "Paris", "Téléphone", "Mario, Luigi, Bowser");
        service.getReunions();
        service.addReunion(reunion);
    }

    @Test
    public void onStartApplication_ThereIsNoItemsToShow(){
        onView(allOf(withId(R.id.reunion_info_textView),
                childAtPosition(childAtPosition(
                        withId(R.id.reunion_list_recyclerview), 0),
                        1), not(isDisplayed())));
    }

    @Test
    public void checkIfAllViewsAreLoaded(){
        onView(withId(R.id.add_reunion_fab)).check(matches(isDisplayed()));
        onView(withId(R.id.reunion_list_recyclerview)).check(matches(isDisplayed()));
        onView(withId(R.id.reunion_toolbar)).check(matches(isDisplayed()));
    }
    @Test
    public void clickingOnFAB_shouldGiveAlertDialogToAddNewReunion() {
        //Check if floating action button exists
         onView(allOf(withId(R.id.add_reunion_fab), childAtPosition(childAtPosition(
                        withId(android.R.id.content), 0), 2),
                        isDisplayed())).check(matches(isDisplayed()));
         //perform a click on FAB
        onView(allOf(withId(R.id.add_reunion_fab),
                childAtPosition(childAtPosition(
                        withId(android.R.id.content), 0), 2),
                isDisplayed())).perform(click());
        //check if dialog tile in new reunion dialog is displayed
        onView(withText("Ajoutez une réunion")).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void fillingNewReunionDialogAndClickAdd_shouldGiveRecyclerViewNewItem(){
        service.deleteReunion(reunion);
        //Check if floating action button exists
        onView(allOf(withId(R.id.add_reunion_fab), childAtPosition(childAtPosition(
                withId(android.R.id.content), 0), 2),
                isDisplayed())).check(matches(isDisplayed()));
        //perform a click on FAB
        onView(allOf(withId(R.id.add_reunion_fab),
                childAtPosition(childAtPosition(
                        withId(android.R.id.content), 0), 2),
                isDisplayed())).perform(click());
        //check if dialog tile in new reunion dialog is displayed
        onView(withText("Ajoutez une réunion")).inRoot(isDialog()).check(matches(isDisplayed()));
        //fill subject textView with String
        onView(withId(R.id.subject_editText)).inRoot(isDialog())
                .check(matches(isDisplayed())).perform(replaceText("Répète"));
        // perform a click on textView to select time
        onView(withId(R.id.txtView_time)).inRoot(isDialog()).perform(click());
        //set time
        onView(withClassName(Matchers.equalTo(android.widget.TimePicker.class.getName()))).inRoot(isDialog())
                .check(matches(isDisplayed())).perform(PickerActions.setTime(2, 10));
        //perform click on positive button
        onView(withText("OK")).inRoot(isDialog()).perform(click());
        //check if time textview matches with time string
        onView(withId(R.id.txtView_time)).inRoot(isDialog())
                .check(matches(isDisplayed())).check(matches(withText("2:10")));
        //check if textview is displayed and perform a click on textview to set date
        onView(withId(R.id.txtView_date)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        // set date
        onView(withClassName(Matchers.equalTo(android.widget.DatePicker.class.getName())))
                .perform(PickerActions.setDate(2019, 10, 10));
        //perform click on positive button
        onView(withText("OK")).inRoot(isDialog()).perform(click());
        //check if date textView is filled with selected date
        onView(withId(R.id.txtView_date)).inRoot(isDialog())
                .check(matches(isDisplayed())).check(matches(withText("10/10/2019")));
        //set Reunion Place
        onView(withId(R.id.place_Spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Salle 2"))).inRoot(isPlatformPopup()).perform(click());
        //check if spinner selection equals to user selected place
        onView(withId(R.id.place_Spinner)).check(matches(withSpinnerText(containsString("Salle 2"))));
        //fill partipants textView with email String
        onView(withId(R.id.participant_editText)).inRoot(isDialog())
                .check(matches(isDisplayed())).perform(replaceText("marco@lamzon.com"));
        //perform a click on positive button and add reunion
        onView(withText("Ajouter")).inRoot(isDialog()).perform(click());
        //check if reunion is displayed with email string
        onView(allOf(withId(R.id.reunion_info_textView), withText("marco@lamzon.com"), childAtPosition(childAtPosition(
                withId(R.id.reunion_list_recyclerview), 0),
                1), (isDisplayed())));
    }

    @Test
    public void deletingReunion_recyclerViewShouldRemoveSelectedReunion(){
        //perform a click on FAB
        ViewInteraction textView = onView(allOf(withId(R.id.reunion_info_textView),
                childAtPosition(childAtPosition(
                        withId(R.id.reunion_list_recyclerview), 0),
                        1), isDisplayed()));
        //check if description matches to neighbour at position 0
        textView.check(matches(withText(service.getReunions().get(0).getDescription())));
        //cherck if delete button is displayed and perform a click
        onView(allOf(withId(R.id.reunion_list_delete_button), childAtPosition(
                childAtPosition(withId(R.id.reunion_list_recyclerview),
                        0), 3), isDisplayed())).perform(click());
        //check if reuinion is deleted and recyclerView is empty
        onView(allOf(withId(R.id.reunion_info_textView), childAtPosition(childAtPosition(
                        withId(R.id.reunion_list_recyclerview), 0),
                        1), not(isDisplayed())));
    }

    @Test
    public void ifReunionInfoIsNotCompletelyFilledWithAllInfo_createNewReunionDialogShouldReload(){
        //check if FAB is displayed
        onView(allOf(withId(R.id.add_reunion_fab), childAtPosition(childAtPosition(
                withId(android.R.id.content), 0), 2),
                isDisplayed())).check(matches(isDisplayed()));
        //perform a click on FAB
        onView(allOf(withId(R.id.add_reunion_fab),
                childAtPosition(childAtPosition(
                        withId(android.R.id.content), 0), 2),
                isDisplayed())).perform(click());
        //check if dialog tile in new reunion dialog is displayed
        onView(withText("Ajoutez une réunion")).inRoot(isDialog()).check(matches(isDisplayed()));
        //perform a click on positive button without filling info
        onView(withText("Ajouter")).inRoot(isDialog()).perform(click());
        //check that create new reunion dialog is displayed with title text
        onView(withText("Ajoutez une réunion")).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void ifReunionParticipantsEmailDoesNotContainsEmailChar_AddNewReunionDialogShouldReload(){
        service.deleteReunion(reunion);
        //Check if floating action button exists
        onView(allOf(withId(R.id.add_reunion_fab), childAtPosition(childAtPosition(
                withId(android.R.id.content), 0), 2),
                isDisplayed())).check(matches(isDisplayed()));
        //perform a click on FAB
        onView(allOf(withId(R.id.add_reunion_fab),
                childAtPosition(childAtPosition(
                        withId(android.R.id.content), 0), 2),
                isDisplayed())).perform(click());
        //check if dialog tile in new reunion dialog is displayed
        onView(withText("Ajoutez une réunion")).inRoot(isDialog()).check(matches(isDisplayed()));
        //fill subject textView with String
        onView(withId(R.id.subject_editText)).inRoot(isDialog())
                .check(matches(isDisplayed())).perform(replaceText("Répète"));
        // perform a click on textView to select time
        onView(withId(R.id.txtView_time)).inRoot(isDialog()).perform(click());
        //set time
        onView(withClassName(Matchers.equalTo(android.widget.TimePicker.class.getName()))).inRoot(isDialog())
                .check(matches(isDisplayed())).perform(PickerActions.setTime(2, 10));
        //perform click on positive button
        onView(withText("OK")).inRoot(isDialog()).perform(click());
        //check if time textview matches with time string
        onView(withId(R.id.txtView_time)).inRoot(isDialog())
                .check(matches(isDisplayed())).check(matches(withText("2:10")));
        //check if textview is displayed and perform a click on textview to set date
        onView(withId(R.id.txtView_date)).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        // set date
        onView(withClassName(Matchers.equalTo(android.widget.DatePicker.class.getName())))
                .perform(PickerActions.setDate(2019, 10, 10));
        //perform click on positive button
        onView(withText("OK")).inRoot(isDialog()).perform(click());
        //check if date textView is filled with selected date
        onView(withId(R.id.txtView_date)).inRoot(isDialog())
                .check(matches(isDisplayed())).check(matches(withText("10/10/2019")));
        //set Reunion Place
        onView(withId(R.id.place_Spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)),
                is("Salle 2"))).inRoot(isPlatformPopup()).perform(click());
        //check if spinner selection equals to user selected place
        onView(withId(R.id.place_Spinner)).check(matches(withSpinnerText(containsString("Salle 2"))));
        //fill partipants textView with email String
        onView(withId(R.id.participant_editText)).inRoot(isDialog())
                .check(matches(isDisplayed())).perform(replaceText("marco"));
        //perform a click on positive button and add reunion
        onView(withText("Ajouter")).inRoot(isDialog()).perform(click());
        //check if create new reunion dialog is reloaded
        onView(withText("Ajoutez une réunion")).inRoot(isDialog()).check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
