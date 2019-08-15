package com.leobro.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

class RandomHelper {

	private static final int START_WORK_HOUR = 9;
	private static final int END_WORK_HOUR = 17;

	private static final String[] FIRST_NAMES = new String[]{"Gillian", "Kevin", "Arthur", "Danny"};
	private static final String[] LAST_NAMES = new String[]{"Anderson", "Bacon", "Cohn", "DeVito", "Ericson", "Ford"};

	private static final Random random = new Random();

	static List<Appointment> getRandomAppointments(int quantity, LocalDate endDate) {
		int roundDays = (int) ChronoUnit.DAYS.between(LocalDate.now(), endDate);
		List<LocalDateTime> slots = new ArrayList<>();

		int nextHour = LocalDateTime.now().plusHours(1).getHour();
		int startHour = START_WORK_HOUR;
		if (nextHour > START_WORK_HOUR) {
			startHour = nextHour;
		}

		if (nextHour < END_WORK_HOUR) {
			for (int workHour = startHour; workHour < END_WORK_HOUR; workHour++) {
				slots.add(LocalDateTime.now().withHour(workHour).truncatedTo(ChronoUnit.HOURS));
			}
		}

		LocalDateTime workDay = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		for (int day = 1; day <= roundDays; day++) {
			workDay = workDay.plusDays(1);

			for (int workHour = START_WORK_HOUR; workHour < END_WORK_HOUR; workHour++) {
				slots.add(workDay.withHour(workHour));
			}
		}

		List<Integer> slotIndices = getRandomIntegersInRange(quantity, slots.size() - 1);

		List<Appointment> apps = generateAppointments(slots, slotIndices);
		return apps;
	}

	private static List<Appointment> generateAppointments(List<LocalDateTime> slots, List<Integer> slotIndices) {
		List<Appointment> apps = new ArrayList<>();
		int firstIndex = 0;
		int lastIndex = 0;

		for (int slotIndex : slotIndices) {
			Appointment app = new Appointment();

			app.setClientName(FIRST_NAMES[firstIndex++] + " " + LAST_NAMES[lastIndex++]);
			app.setTime(slots.get(slotIndex));
			app.setPrice(getOneRandomIntegerInRange(1, 20) * 10);
			app.setStatus(Appointment.AppStatus.PASS);

			if (firstIndex == 4) {
				firstIndex = 0;
			}
			if (lastIndex == 6) {
				lastIndex = 0;
			}

			apps.add(app);
		}
		return apps;
	}

	private static List<Integer> getRandomIntegersInRange(long count, int max) {
		return random.ints(count, 0, max + 1)
				.sorted()
				.distinct()
				.boxed().collect(Collectors.toList());
	}

	private static int getOneRandomIntegerInRange(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}
}
