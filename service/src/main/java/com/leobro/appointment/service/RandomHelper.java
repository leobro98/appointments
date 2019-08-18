package com.leobro.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Helper class for creation random appointments for testing purposes.
 * <p>Appointments are created for the specified date range only within work hours. For the sake of simplicity,
 * they are created only to a round hour, but randomly. Thus, each round work hour represents a slot for an appointment.
 * If two appointments would take the same time slot, one of them is neglected.
 */
class RandomHelper {

	private static final int START_WORK_HOUR = 9;
	private static final int END_WORK_HOUR = 17;

	private static final String[] FIRST_NAMES = new String[]{"Gillian", "Kevin", "Arthur", "Danny"};
	private static final String[] LAST_NAMES = new String[]{"Anderson", "Bacon", "Cohn", "DeVito", "Ericson", "Ford"};

	private static final Random random = new Random();

	static List<Appointment> getRandomAppointments(int quantity, LocalDate endDate) {
		List<LocalDateTime> slots = createAppointmentSlotsForToday();
		slots.addAll(createAppointmentSlotsForNextFullDays(endDate));

		List<Integer> slotIndices = getRandomIntegers(quantity, slots.size() - 1);

		return generateAppointments(slots, slotIndices);
	}

	private static List<LocalDateTime> createAppointmentSlotsForToday() {
		List<LocalDateTime> slots = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();

		int nextHour = now.plusHours(1).getHour();
		int startHour = START_WORK_HOUR;
		if (nextHour > START_WORK_HOUR) {
			startHour = nextHour;
		}

		if (nextHour < END_WORK_HOUR) {
			for (int hour = startHour; hour < END_WORK_HOUR; hour++) {
				slots.add(now.withHour(hour).truncatedTo(ChronoUnit.HOURS));
			}
		}
		return slots;
	}

	private static List<LocalDateTime> createAppointmentSlotsForNextFullDays(LocalDate endDate) {
		List<LocalDateTime> slots = new ArrayList<>();
		int fullDayCount = (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
		LocalDateTime day = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

		for (int dayIndex = 1; dayIndex <= fullDayCount; dayIndex++) {
			day = day.plusDays(1);

			for (int hour = START_WORK_HOUR; hour < END_WORK_HOUR; hour++) {
				slots.add(day.withHour(hour));
			}
		}
		return slots;
	}

	private static List<Integer> getRandomIntegers(long count, int max) {
		return random.ints(count, 0, max + 1)
				.sorted()
				.distinct()
				.boxed().collect(Collectors.toList());
	}

	private static List<Appointment> generateAppointments(List<LocalDateTime> slots, List<Integer> slotIndices) {
		List<Appointment> apps = new ArrayList<>();

		for (int slotIndex : slotIndices) {
			Appointment app = new Appointment();

			app.setClientName(getRandomName());
			app.setTime(slots.get(slotIndex));
			app.setPrice(getOneRandomIntegerInRange(1, 20) * 10);
			app.setStatus(Appointment.AppStatus.PASS);

			apps.add(app);
		}
		return apps;
	}

	private static String getRandomName() {
		return FIRST_NAMES[getOneRandomIntegerInRange(0, 3)] + " "
				+ LAST_NAMES[getOneRandomIntegerInRange(0, 5)];
	}

	private static int getOneRandomIntegerInRange(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}
}
