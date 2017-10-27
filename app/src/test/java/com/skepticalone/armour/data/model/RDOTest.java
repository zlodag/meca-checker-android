package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import org.junit.Test;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RDOTest extends RosteredShiftTest {

    @Test
    public void sanity() {
        for (int i = 0; i < 12; i++) {
            assertTrue(shiftSpecs.add(new ShiftSpec(i, 8, 0, 16, 0)));
        }
        List<RosteredShift> rosteredShifts = getRosteredShifts(RosteredShiftTest.NONE_COMPLIANT);
        RDO rdo = new RDO(rosteredShifts, true);
        for (int i = 0; i < rosteredShifts.size(); i++) {
            RosteredShift shift = rosteredShifts.get(i);
            assertNull(shift.getCompliance().getRosteredDayOff());
            LocalDate shiftDate = shift.getShiftData().getStart().toLocalDate();
            assertTrue(rdo.getWorkedDates().contains(shiftDate));
            assertFalse(rdo.getWeekdayRosteredDaysOff().contains(shiftDate));
        }
        assertEquals(12, rdo.getWorkedDates().size());
        assertTrue(rdo.getWeekdayRosteredDaysOff().isEmpty());
    }

    private void testDaysOff(boolean strict, @NonNull int[] dates, @NonNull int[] assignedWeekdayRDOsIndices, @NonNull int[] weekdayRDOs) {
        shiftSpecs.clear();
        String message = "testDaysOff() called with: strict = [" + strict + "], dates = [" + Arrays.toString(dates) + "], assignedWeekdayRDOsIndex = [" + Arrays.toString(assignedWeekdayRDOsIndices) + "], weekdayRDOs = [" + Arrays.toString(weekdayRDOs) + "]";
        assertEquals(message, assignedWeekdayRDOsIndices.length, weekdayRDOs.length);
        for (int daysAfterStart : dates
                ) {
            shiftSpecs.add(new ShiftSpec(daysAfterStart, 8, 0, 16, 0));
        }
        List<RosteredShift> rosteredShifts = getRosteredShifts(RosteredShiftTest.NONE_COMPLIANT);
        for (int assignedWeekdayRdoIndex : assignedWeekdayRDOsIndices) {
            assertTrue(message, assignedWeekdayRdoIndex < rosteredShifts.size());
        }
        RDO rdo = new RDO(rosteredShifts, strict);
        for (int shiftIndex = 0; shiftIndex < rosteredShifts.size(); shiftIndex++) {
            RosteredShift shift = rosteredShifts.get(shiftIndex);
            boolean shouldHaveAssignedRDO = false;
            for (int assignedWeekdayRdoIndex = 0; !shouldHaveAssignedRDO && assignedWeekdayRdoIndex < assignedWeekdayRDOsIndices.length; assignedWeekdayRdoIndex++) {
                if (shiftIndex == assignedWeekdayRDOsIndices[assignedWeekdayRdoIndex]) {
                    shouldHaveAssignedRDO = true;
                    assertTrue(message, shift.getShiftData().getStart().getDayOfWeek() == DayOfWeek.SATURDAY || shift.getShiftData().getStart().getDayOfWeek() == DayOfWeek.SUNDAY);
                    LocalDate rosteredDayOff = ShiftSpec.START_DATE.plusDays(weekdayRDOs[assignedWeekdayRdoIndex]);
                    assertEquals(message + ", shift = " + shift.getShiftData().getStart().toLocalDate().toString(), rosteredDayOff, shift.getCompliance().getRosteredDayOff());
                    assertTrue(message, rdo.getWeekdayRosteredDaysOff().contains(rosteredDayOff));
                }
            }
            if (!shouldHaveAssignedRDO) {
                assertNull(message, shift.getCompliance().getRosteredDayOff());
            }
        }
        assertEquals(message, assignedWeekdayRDOsIndices.length, rdo.getWeekdayRosteredDaysOff().size());
    }

    @Test
    public void oneDayOff() {
        for (int i = 0; i < 12; i++) {
            if (i == 5 || i == 6) continue;
            int[] dates = new int[11];
            for (int j = 0; j < 12; j++) {
                if (j == i) continue;
                dates[j < i ? j : j - 1] = j;
            }
            testDaysOff(false, dates, new int[]{i < 5 ? 4 : 5}, new int[]{i});
        }
    }

    @Test
    public void twoDaysOffStartFirstWeek() {
        testDaysOff(false, new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, new int[]{3, 4}, new int[]{0, 1});
        testDaysOff(true, new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, new int[]{3, 4}, new int[]{0, 1});
    }

    @Test
    public void twoDaysOffMiddleFirstWeek() {
        testDaysOff(false, new int[]{0, 3, 4, 5, 6, 7, 8, 9, 10, 11}, new int[]{3, 4}, new int[]{1, 2});
        testDaysOff(true, new int[]{0, 3, 4, 5, 6, 7, 8, 9, 10, 11}, new int[]{}, new int[]{});
    }

    @Test
    public void twoDaysOffEndFirstWeek() {
        testDaysOff(false, new int[]{0, 1, 2, 5, 6, 7, 8, 9, 10, 11}, new int[]{3, 4}, new int[]{3, 4});
        testDaysOff(true, new int[]{0, 1, 2, 5, 6, 7, 8, 9, 10, 11}, new int[]{}, new int[]{});
    }

    @Test
    public void twoDaysOffStartSecondWeek() {
        testDaysOff(false, new int[]{0, 1, 2, 3, 4, 5, 6, 9, 10, 11}, new int[]{5, 6}, new int[]{7, 8});
        testDaysOff(true, new int[]{0, 1, 2, 3, 4, 5, 6, 9, 10, 11}, new int[]{}, new int[]{});
    }

    @Test
    public void twoDaysOffMiddleSecondWeek() {
        testDaysOff(false, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 11}, new int[]{5, 6}, new int[]{9, 10});
        testDaysOff(true, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 11}, new int[]{}, new int[]{});
    }

    @Test
    public void twoDaysOffEndSecondWeek() {
        testDaysOff(false, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, new int[]{5, 6}, new int[]{10, 11});
        testDaysOff(true, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, new int[]{5, 6}, new int[]{10, 11});
    }

    @Test
    public void threeScatteredDaysOff() {
        testDaysOff(false, new int[]{1, 2, 3, 5, 6, 7, 9, 10}, new int[]{3, 4}, new int[]{0, 4});
        testDaysOff(true, new int[]{1, 2, 3, 5, 6, 7, 9, 10}, new int[]{3, 4}, new int[]{0, 11});
    }

}