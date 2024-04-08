package com.example.seqr;

//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.replaceText;
//import static androidx.test.espresso.action.ViewActions.scrollTo;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.testing.FragmentScenario;
//import androidx.test.espresso.Espresso;
//import androidx.test.espresso.matcher.ViewMatchers;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.filters.LargeTest;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//
//import com.example.seqr.events.CEventDetailFragment;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class CEventDetailFragmentTest {
//    @Before
//    public void launchFragment(){
//        //
//        FragmentScenario.launchInContainer(CEventDetailFragment.class,null);
//
//    }
//
//    @Test
//    public void userInputEventDetails(){
//        //This will find the eventNameInput and enter the following text into the field
//        onView(withId(R.id.eventNameInput)).perform(scrollTo(), replaceText("Lebron Testing Event Name"))
//                .check(matches(withText("Lebron Testing Event Name")));
//
//        //This will find the locationInput and enter the following text into the field
//        onView(withId(R.id.eventLocationInput)).perform(scrollTo(),replaceText("Lebron Test Location"))
//                .check(matches(withText("Lebron Test Location")));
//
//        //This will find the eventDesc and enter the following text into the fields
//        onView(withId(R.id.eventDescriptionInput)).perform(scrollTo(),replaceText("Lebrons Testing description"))
//                .check(matches(withText("Lebrons Testing description")));
//
//        //Check if you can go to next fragment, which should be able to happen it should still be on the CEventDetail fragment
//        //So check if the eventName is still on curr fragment so we can tell if we switched or not
//        //This shouldn't switch because we didn't pick a date or time
//        onView(withId(R.id.cEventDetailNextButton)).perform(scrollTo(),click());
//        onView(withId(R.id.eventNameInput)).check(matches(isDisplayed()));
//
//
//
//
//
//
//
//
//    }
//
//}
