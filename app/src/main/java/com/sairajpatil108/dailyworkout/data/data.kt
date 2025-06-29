package com.sairajpatil108.dailyworkout.data

// Data classes for workout structure
data class WorkoutPlan(
	val weeklySchedule: Map<String, List<Exercise>>
)

data class Exercise(
	val exerciseName: String,
	val targetMuscles: String,
	val sets: String,
	val reps: String,
	val weightGuidance: String,
	val properPosture: String,
	val dos: List<String>,
	val donts: List<String>,
	val tutorialVideos: List<String>
)

// Sample data structure with your workout plan
class WorkoutData {
	companion object {
		fun getWorkoutPlan(): WorkoutPlan {
			return WorkoutPlan(
				weeklySchedule = mapOf(
					"Monday" to listOf(
						Exercise(
							exerciseName = "Incline Chest Press Machine",
							targetMuscles = "Upper Chest (Upper Pectorals), Front Shoulders (Anterior Deltoids), Triceps",
							sets = "3",
							reps = "10-12",
							weightGuidance = "Start with a weight allowing 10-12 reps with good form, challenging last 2 reps.",
							properPosture = "Adjust the seat so that when seated, the handles are approximately at nipple height. Keep the head back against the pad and feet firmly planted on the ground. Elbows should point downwards, aligning with the machine's upward incline path. Maintain a neutral spine and a tight core.",
							dos = emptyList(), // Add your dos here
							donts = emptyList(), // Add your donts here
							tutorialVideos = emptyList() // Add video URLs here
						),
						Exercise(
							exerciseName = "Machine Shoulder Press",
							targetMuscles = "Shoulders (Deltoids), Triceps",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit with feet flat on the ground and the back firmly against the pad. Ensure the handles are at a comfortable height to initiate the movement.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Pec Deck Fly (Machine Fly)",
							targetMuscles = "Chest (Pectoralis Major)",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Adjust the seat and set the height so that the elbows are at chest level when seated. Place the arms on the pads or grips with elbows bent at approximately 90 degrees. Keep the feet flat on the floor for stability. Maintain an elevated chest position.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Tricep Pushdown (Cable-Based)",
							targetMuscles = "Triceps (all three heads)",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand facing the cable machine with a rope, straight bar, or V-bar attachment connected to a high pulley. Grip the attachment with an overhand grip, keeping the elbows tucked close to the sides. Lean slightly forward from the hips, maintaining a straight back and engaged core.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Overhead Triceps Extension (Cable)",
							targetMuscles = "Triceps (Long Head)",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Set the rope attachment at the top of the cable system. Stand facing away from the machine, holding the rope with both hands. Place one foot forward against the machine for leverage and lean slightly forward. Keep the elbows high and close to the head.",
							dos = listOf(
								"Extend the arms overhead, pushing the rope away from the head until the arms are straight, focusing on contracting the triceps.",
								"Allow for a full range of motion, stretching the triceps at the bottom.",
								"At the top of the movement, if using a rope, slightly rotate the hands outwards to maximize triceps activation.",
								"Control the weight as it is slowly returned to the starting position."
							),
							donts = emptyList(),
							tutorialVideos = emptyList()
						)
					),
					"Tuesday" to listOf(
						Exercise(
							exerciseName = "Seated Cable Row Machine",
							targetMuscles = "Upper Back (Rhomboids, Trapezius), Lats, Biceps",
							sets = "3",
							reps = "10-12",
							weightGuidance = "Start with a weight allowing 10-12 reps with good form, challenging last 2 reps.",
							properPosture = "Sit upright on the bench with knees slightly bent and feet firmly on the foot platform. Grasp the handles (typically a V-bar or parallel grip handles) with arms extended forward. Maintain a straight back and retracted shoulders.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Assisted Dip Machine",
							targetMuscles = "Lower Chest, Triceps, Shoulders (Back/Biceps emphasis)",
							sets = "3",
							reps = "8-10",
							weightGuidance = "Start with a weight allowing 8-10 reps with good form, challenging last 2 reps.",
							properPosture = "Place both hands on the dip bars. Step or kneel onto the provided pad. To emphasize the chest, lean slightly forward. To isolate the triceps, maintain an upright posture. Keep the elbows tucked in and the shoulders depressed and retracted.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Machine Bicep Curl",
							targetMuscles = "Biceps",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit comfortably on the machine with the back pressed against the pad. Grip the handles with an underhand grip (palms facing up). Ensure the elbows are aligned with the machine's pivot point.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Cable Face Pull",
							targetMuscles = "Shoulders (Rear Deltoids), Upper Back (Traps, Rhomboids)",
							sets = "3",
							reps = "15-20",
							weightGuidance = "Start with a weight allowing 15-20 reps with good form, challenging last 2 reps.",
							properPosture = "Set the cable pulley to face height. Attach a rope attachment. Take a few steps back from the machine to create tension on the cable, and extend the arms in front of the body. Feet should be shoulder-width apart or in a split stance for stability. Lean back slightly and engage the core. Keep the elbows high and flared out.",
							dos = listOf(
								"Initiate the movement by pulling the rope towards the forehead/ears, driving the elbows up and back.",
								"Focus on squeezing the shoulder blades together at the top of the movement.",
								"Allow the shoulders to round forward slightly at the start of the movement before pulling back.",
								"Maintain a controlled motion from start to finish, avoiding momentum.",
								"Ensure the elbow remains outside the shoulder throughout the range of motion."
							),
							donts = emptyList(),
							tutorialVideos = emptyList()
						)
					),
					"Wednesday" to listOf(
						Exercise(
							exerciseName = "Leg Extension Machine",
							targetMuscles = "Quadriceps",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit on the machine with the back pressed against the pad. Position the ankles under the roller pad, ensuring the pad rests just above the ankles. Knees should align with the rotating axis of the machine. Place hands on the side handles for stability.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Seated Leg Curl Machine",
							targetMuscles = "Hamstrings",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit upright on the machine with the back against the pad. Adjust the pad so it rests comfortably above the back of the heels. Hands can hold onto the side handles or push into the pad for stability. Maintain a neutral spine.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Calf Raise Machine (Seated/Standing)",
							targetMuscles = "Calves (Gastrocnemius and Soleus)",
							sets = "3",
							reps = "15-20",
							weightGuidance = "Start with a weight allowing 15-20 reps with good form, challenging last 2 reps.",
							properPosture = "For a seated machine, sit with knees comfortably positioned under the pads and the balls of the feet on the platform, heels hanging off. For a standing machine, place the balls of the feet on the platform, heels hanging off, and keep the legs straight but not locked.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Hack Squat Machine",
							targetMuscles = "Quadriceps (primary), Glutes, Hamstrings, Calves, Lower Back",
							sets = "3",
							reps = "10-12",
							weightGuidance = "Start with a weight allowing 10-12 reps with good form, challenging last 2 reps.",
							properPosture = "Position the body under the shoulder pads with the back flat against the backrest. Place the feet shoulder-width apart on the platform. For increased quad activation, place feet lower on the platform; for greater glute activation, place them higher.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Ab Crunch Machine",
							targetMuscles = "Core, Abdominals, Obliques",
							sets = "3",
							reps = "15-20",
							weightGuidance = "Start with a weight allowing 15-20 reps with good form, challenging last 2 reps.",
							properPosture = "Adjust the seat and weight to preference. Sit back, ensuring the shoulders are in line with the pad. Place the elbows into the pads and relax the arms behind the body. Maintain a 90-degree angle from the ankles to knees to hips.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Cable Kneeling Crunch",
							targetMuscles = "Abs, Obliques, Hip Flexors, Lower Back",
							sets = "3",
							reps = "15-20",
							weightGuidance = "Start with a weight allowing 15-20 reps with good form, challenging last 2 reps.",
							properPosture = "Set the radial arms of the cable machine at a high position and attach two D handles or a rope handle. Kneel on the front plate (a mat may be used for comfort), facing the cable. Grab a handle with each hand at head height.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						)
					),
					"Thursday" to listOf(
						Exercise(
							exerciseName = "Incline Chest Press Machine",
							targetMuscles = "Upper Chest (Upper Pectorals), Front Shoulders (Anterior Deltoids), Triceps",
							sets = "3",
							reps = "10-12",
							weightGuidance = "Start with a weight allowing 10-12 reps with good form, challenging last 2 reps.",
							properPosture = "Adjust the seat so that when seated, the handles are approximately at nipple height. Keep the head back against the pad and feet firmly planted on the ground. Elbows should point downwards, aligning with the machine's upward incline path. Maintain a neutral spine and a tight core.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Machine Shoulder Press",
							targetMuscles = "Shoulders (Deltoids), Triceps",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit with feet flat on the ground and the back firmly against the pad. Ensure the handles are at a comfortable height to initiate the movement.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Pec Deck Fly (Machine Fly)",
							targetMuscles = "Chest (Pectoralis Major)",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Adjust the seat and set the height so that the elbows are at chest level when seated. Place the arms on the pads or grips with elbows bent at approximately 90 degrees. Keep the feet flat on the floor for stability. Maintain an elevated chest position.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Tricep Pushdown (Cable-Based)",
							targetMuscles = "Triceps (all three heads)",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand facing the cable machine with a rope, straight bar, or V-bar attachment connected to a high pulley. Grip the attachment with an overhand grip, keeping the elbows tucked close to the sides. Lean slightly forward from the hips, maintaining a straight back and engaged core.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Overhead Triceps Extension (Cable)",
							targetMuscles = "Triceps (Long Head)",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Set the rope attachment at the top of the cable system. Stand facing away from the machine, holding the rope with both hands. Place one foot forward against the machine for leverage and lean slightly forward. Keep the elbows high and close to the head.",
							dos = listOf(
								"Extend the arms overhead, pushing the rope away from the head until the arms are straight, focusing on contracting the triceps.",
								"Allow for a full range of motion, stretching the triceps at the bottom.",
								"At the top of the movement, if using a rope, slightly rotate the hands outwards to maximize triceps activation.",
								"Control the weight as it is slowly returned to the starting position."
							),
							donts = emptyList(),
							tutorialVideos = emptyList()
						)
					),
					"Friday" to listOf(
						Exercise(
							exerciseName = "Seated Cable Row Machine",
							targetMuscles = "Upper Back (Rhomboids, Trapezius), Lats, Biceps",
							sets = "3",
							reps = "10-12",
							weightGuidance = "Start with a weight allowing 10-12 reps with good form, challenging last 2 reps.",
							properPosture = "Sit upright on the bench with knees slightly bent and feet firmly on the foot platform. Grasp the handles (typically a V-bar or parallel grip handles) with arms extended forward. Maintain a straight back and retracted shoulders.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Assisted Dip Machine",
							targetMuscles = "Lower Chest, Triceps, Shoulders (Back/Biceps emphasis)",
							sets = "3",
							reps = "8-10",
							weightGuidance = "Start with a weight allowing 8-10 reps with good form, challenging last 2 reps.",
							properPosture = "Place both hands on the dip bars. Step or kneel onto the provided pad. To emphasize the chest, lean slightly forward. To isolate the triceps, maintain an upright posture. Keep the elbows tucked in and the shoulders depressed and retracted.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Machine Bicep Curl",
							targetMuscles = "Biceps",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit comfortably on the machine with the back pressed against the pad. Grip the handles with an underhand grip (palms facing up). Ensure the elbows are aligned with the machine's pivot point.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Cable Face Pull",
							targetMuscles = "Shoulders (Rear Deltoids), Upper Back (Traps, Rhomboids)",
							sets = "3",
							reps = "15-20",
							weightGuidance = "Start with a weight allowing 15-20 reps with good form, challenging last 2 reps.",
							properPosture = "Set the cable pulley to face height. Attach a rope attachment. Take a few steps back from the machine to create tension on the cable, and extend the arms in front of the body. Feet should be shoulder-width apart or in a split stance for stability. Lean back slightly and engage the core. Keep the elbows high and flared out.",
							dos = listOf(
								"Initiate the movement by pulling the rope towards the forehead/ears, driving the elbows up and back.",
								"Focus on squeezing the shoulder blades together at the top of the movement.",
								"Allow the shoulders to round forward slightly at the start of the movement before pulling back.",
								"Maintain a controlled motion from start to finish, avoiding momentum.",
								"Ensure the elbow remains outside the shoulder throughout the range of motion."
							),
							donts = emptyList(),
							tutorialVideos = emptyList()
						)
					),
					"Saturday" to listOf(
						Exercise(
							exerciseName = "Leg Extension Machine",
							targetMuscles = "Quadriceps",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit on the machine with the back pressed against the pad. Position the ankles under the roller pad, ensuring the pad rests just above the ankles. Knees should align with the rotating axis of the machine. Place hands on the side handles for stability.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Seated Leg Curl Machine",
							targetMuscles = "Hamstrings",
							sets = "3",
							reps = "12-15",
							weightGuidance = "Start with a weight allowing 12-15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit upright on the machine with the back against the pad. Adjust the pad so it rests comfortably above the back of the heels. Hands can hold onto the side handles or push into the pad for stability. Maintain a neutral spine.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Calf Raise Machine (Seated/Standing)",
							targetMuscles = "Calves (Gastrocnemius and Soleus)",
							sets = "3",
							reps = "15-20",
							weightGuidance = "Start with a weight allowing 15-20 reps with good form, challenging last 2 reps.",
							properPosture = "For a seated machine, sit with knees comfortably positioned under the pads and the balls of the feet on the platform, heels hanging off. For a standing machine, place the balls of the feet on the platform, heels hanging off, and keep the legs straight but not locked.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Hack Squat Machine",
							targetMuscles = "Quadriceps (primary), Glutes, Hamstrings, Calves, Lower Back",
							sets = "3",
							reps = "10-12",
							weightGuidance = "Start with a weight allowing 10-12 reps with good form, challenging last 2 reps.",
							properPosture = "Position the body under the shoulder pads with the back flat against the backrest. Place the feet shoulder-width apart on the platform. For increased quad activation, place feet lower on the platform; for greater glute activation, place them higher.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Ab Crunch Machine",
							targetMuscles = "Core, Abdominals, Obliques",
							sets = "3",
							reps = "15-20",
							weightGuidance = "Start with a weight allowing 15-20 reps with good form, challenging last 2 reps.",
							properPosture = "Adjust the seat and weight to preference. Sit back, ensuring the shoulders are in line with the pad. Place the elbows into the pads and relax the arms behind the body. Maintain a 90-degree angle from the ankles to knees to hips.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						),
						Exercise(
							exerciseName = "Cable Kneeling Crunch",
							targetMuscles = "Abs, Obliques, Hip Flexors, Lower Back",
							sets = "3",
							reps = "15-20",
							weightGuidance = "Start with a weight allowing 15-20 reps with good form, challenging last 2 reps.",
							properPosture = "Set the radial arms of the cable machine at a high position and attach two D handles or a rope handle. Kneel on the front plate (a mat may be used for comfort), facing the cable. Grab a handle with each hand at head height.",
							dos = emptyList(),
							donts = emptyList(),
							tutorialVideos = emptyList()
						)
					),
					"Sunday" to emptyList() // Rest day
				)
			)
		}
	}
}

// WorkoutRepository has been moved to a separate file