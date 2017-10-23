package com.skepticalone.armour.data.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ConstantConditions")
public class ComplianceConfigTest extends RosteredShiftTest {

    @Test
    public void insufficientDurationBetweenShifts() {
        Compliance.Configuration configuration = NONE_COMPLIANT.withCheckDurationBetweenShifts(true);
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 16, 0, 0, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 8, 0, 16, 0)));
        Compliance compliance = getRosteredShifts(configuration).get(1).getCompliance();
        assertTrue(compliance.sufficientDurationBetweenShifts());
        assertTrue(compliance.isCompliant());
        assertTrue(shiftSpecs.remove(new ShiftSpec(1, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 7, 59, 16, 0)));
        compliance = getRosteredShifts(configuration).get(1).getCompliance();
        assertFalse(compliance.sufficientDurationBetweenShifts());
        assertFalse(compliance.isCompliant());
    }

    @Test
    public void exceedsMaximumDurationOverDay() {
        Compliance.Configuration configuration = NONE_COMPLIANT.withCheckDurationOverDay(true);
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 6, 0, 22, 0)));
        Compliance compliance = getRosteredShifts(configuration).get(0).getCompliance();
        assertTrue(compliance.compliesWithMaximumDurationOverDay());
        assertTrue(compliance.isCompliant());
        assertTrue(shiftSpecs.remove(new ShiftSpec(0, 6, 0, 22, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 6, 0, 22, 1)));
        compliance = getRosteredShifts(configuration).get(0).getCompliance();
        assertFalse(compliance.compliesWithMaximumDurationOverDay());
        assertFalse(compliance.isCompliant());
    }

    @Test
    public void exceedsMaximumDurationOverWeek() {
        Compliance.Configuration configuration = NONE_COMPLIANT.withCheckDurationOverWeek(true);
        assertTrue(shiftSpecs.add(new ShiftSpec(0, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(3, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 6, 0, 18, 0)));

        Compliance compliance = getRosteredShifts(configuration).get(6).getCompliance();
        assertTrue(compliance.compliesWithMaximumDurationOverWeek());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 6, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 5, 59, 18, 0)));

        compliance = getRosteredShifts(configuration).get(6).getCompliance();
        assertFalse(compliance.compliesWithMaximumDurationOverWeek());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 5, 59, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 6, 0, 18, 0)));

        compliance = getRosteredShifts(configuration).get(6).getCompliance();
        assertTrue(compliance.compliesWithMaximumDurationOverWeek());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 8, 0, 18, 0)));

        compliance = getRosteredShifts(configuration).get(7).getCompliance();
        assertTrue(compliance.compliesWithMaximumDurationOverWeek());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(-1, 8, 0, 18, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 8, 0, 18, 1)));

        compliance = getRosteredShifts(configuration).get(7).getCompliance();
        assertFalse(compliance.compliesWithMaximumDurationOverWeek());
        assertFalse(compliance.isCompliant());

    }

    @Test
    public void exceedsMaximumDurationOverFortnight() {
        Compliance.Configuration configuration = NONE_COMPLIANT.withCheckDurationOverFortnight(true);

        assertTrue(shiftSpecs.add(new ShiftSpec(0, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(1, 8, 0, 20, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(2, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(3, 8, 0, 20, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 8, 0, 22, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 8, 0, 22, 30)));
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 8, 0, 20, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(8, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(9, 8, 0, 20, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(10, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(13, 7, 0, 19, 1)));

        Compliance compliance = getRosteredShifts(configuration).get(11).getCompliance();
        assertFalse(compliance.compliesWithMaximumDurationOverFortnight());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(13, 7, 0, 19, 1)));
        assertTrue(shiftSpecs.add(new ShiftSpec(13, 7, 0, 19, 0)));

        compliance = getRosteredShifts(configuration).get(11).getCompliance();
        assertTrue(compliance.compliesWithMaximumDurationOverFortnight());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 8, 0, 19, 0)));

        compliance = getRosteredShifts(configuration).get(12).getCompliance();
        assertTrue(compliance.compliesWithMaximumDurationOverFortnight());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(-1, 8, 0, 19, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 8, 1, 19, 1)));

        compliance = getRosteredShifts(configuration).get(12).getCompliance();
        assertFalse(compliance.compliesWithMaximumDurationOverFortnight());
        assertFalse(compliance.isCompliant());
    }

    @Test
    public void consecutiveWeekendsWorked() {

        Compliance.Configuration configuration = NONE_COMPLIANT.withCheckConsecutiveWeekends(true);

        assertTrue(shiftSpecs.add(new ShiftSpec(-1, 8, 0, 16, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(4, 8, 0, 0, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(12, 8, 0, 16, 0)));

        Compliance compliance = getRosteredShifts(configuration).get(2).getCompliance();
        assertTrue(compliance.compliesWithMaximumWeekendsWorked());
        assertTrue(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(4, 8, 0, 0, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(5, 0, 0, 0, 1)));

        compliance = getRosteredShifts(configuration).get(2).getCompliance();
        assertFalse(compliance.compliesWithMaximumWeekendsWorked());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(5, 0, 0, 0, 1)));
        assertTrue(shiftSpecs.add(new ShiftSpec(6, 23, 59, 0, 0)));

        compliance = getRosteredShifts(configuration).get(2).getCompliance();
        assertFalse(compliance.compliesWithMaximumWeekendsWorked());
        assertFalse(compliance.isCompliant());

        assertTrue(shiftSpecs.remove(new ShiftSpec(6, 23, 59, 0, 0)));
        assertTrue(shiftSpecs.add(new ShiftSpec(7, 0, 0, 0, 1)));

        compliance = getRosteredShifts(configuration).get(2).getCompliance();
        assertTrue(compliance.compliesWithMaximumWeekendsWorked());
        assertTrue(compliance.isCompliant());

    }

}