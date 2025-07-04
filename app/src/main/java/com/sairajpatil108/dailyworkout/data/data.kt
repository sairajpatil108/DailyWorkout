package com.sairajpatil108.dailyworkout.data

// Data classes for workout structure
data class WorkoutPlan(
	val weeklySchedule: Map<String, List<Exercise>>
)

data class Exercise(
	val exerciseName: String,
	val targetMuscles: String,
	val sets: Int,
	val reps: Int,
	val weightGuidance: String,
	val properPosture: String,
	val dos: List<String>,
	val donts: List<String>,
	val tutorialVideoUrl: String? = null // Single video URL - easy to add links here
)

// Sample data structure with your workout plan
// TO ADD VIDEO URLS: Simply replace "PLACEHOLDER_VIDEO_URL" with your YouTube URL like this:
// tutorialVideoUrl = "https://www.youtube.com/watch?v=VIDEO_ID"
// OR any other video platform URL

class WorkoutData {
	companion object {
		fun getWorkoutPlan(): WorkoutPlan {
			return WorkoutPlan(
				weeklySchedule = mapOf(
					"Monday" to listOf(
						Exercise(
							exerciseName = "Squat",
							targetMuscles = "Quadriceps, Glutes, Hamstrings",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand with feet shoulder-width apart and toes slightly turned out. Keep chest up, back straight, core tight. Sit back into hips as you bend knees.",
							dos = listOf("Drive through heels", "Push knees out in line with toes", "Keep chest up and gaze forward"),
							donts = listOf("Let knees collapse inward", "Round or arch the lower back", "Lean too far forward"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Lunge",
							targetMuscles = "Quadriceps, Hamstrings, Glutes, Adductors",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Step forward with one leg, keeping torso upright. Bend both knees until front knee is ~90° and shin vertical. Keep back straight and core engaged.",
							dos = listOf("Keep front shin vertical", "Push knee outward to avoid knee collapse", "Maintain an upright chest"),
							donts = listOf("Don't lean forward at waist", "Don't let lunging knee go past toes", "Don't add weight until form is solid"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Leg Extension",
							targetMuscles = "Quadriceps",
							sets = 3,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit with back against pad and adjust seat so knees align with machine pivot. Position feet under padded bar.",
							dos = listOf("Extend legs fully to contract quads", "Control the weight on the way down", "Sit upright with hips on seat"),
							donts = listOf("Don't lock knees out forcefully", "Don't lift hips off seat", "Don't use momentum"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Standing Calf Raise",
							targetMuscles = "Calves (Gastrocnemius, Soleus)",
							sets = 3,
							reps = 15,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand on a raised platform with balls of feet, heels hanging. Keep knees slightly bent and core engaged.",
							dos = listOf("Push through balls of feet to raise heels", "Hold top contraction briefly", "Use slow, controlled movement"),
							donts = listOf("Don't bounce or use momentum", "Don't lean forward or backward", "Don't lock knees"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Seated Calf Raise",
							targetMuscles = "Calves (Soleus focus)",
							sets = 3,
							reps = 15,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit with knees under pads and balls of feet on platform. Keep back straight and core engaged.",
							dos = listOf("Extend ankles fully and pause at top", "Lower heels below platform for full stretch", "Maintain steady, controlled tempo"),
							donts = listOf("Don't bounce at bottom or top", "Don't lift thighs off pads", "Don't rush reps"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Leg Curl",
							targetMuscles = "Hamstrings (and calves)",
							sets = 3,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Lie face down on bench with padded lever just above heels. Keep hips down and torso flat on bench.",
							dos = listOf("Curl heels toward glutes to fully flex hamstrings", "Keep hips pressed down on bench", "Return to full stretch with control"),
							donts = listOf("Don't lift hips or arch back during movement", "Don't use excessive weight causing jerking", "Don't drop weight quickly"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						)
					),
					"Tuesday" to listOf(
						Exercise(
							exerciseName = "Flat Bench Press",
							targetMuscles = "Pectoralis Major, Anterior Deltoids, Triceps",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Lie back on bench with feet flat on floor. Grip bar slightly wider than shoulders, engage core and glutes.",
							dos = listOf("Keep elbows at ~45° from body", "Drive feet into floor for stability", "Lower bar to nipple level under control"),
							donts = listOf("Don't flare elbows wide", "Don't bounce bar off chest", "Don't arch back excessively"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Overhead Press",
							targetMuscles = "Deltoids (Shoulders), Triceps",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand with feet hip-width. Hold barbell at collarbone, brace core and glutes. Press bar overhead in a straight path.",
							dos = listOf("Keep elbows under wrists and in front of body", "Engage abs and glutes throughout", "Exhale as you press up"),
							donts = listOf("Don't arch lower back (keep core tight)", "Don't flare elbows too far out", "Don't bend head excessively backward"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Low Cable Row",
							targetMuscles = "Upper and Middle Back (Lats, Rhomboids, Trapezius), Biceps",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit upright with knees slightly bent, chest out, and feet against platform. Grasp handle with arms extended.",
							dos = listOf("Pull handle to lower abdomen while squeezing shoulder blades", "Keep back straight and chest up", "Use arms and back muscles, not momentum"),
							donts = listOf("Don't round the lower back", "Don't lean back excessively", "Don't use torso swing to lift weight"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Lat Pulldown",
							targetMuscles = "Latissimus Dorsi, Teres Major (Back), Biceps",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit with thighs secured under pad, lean back slightly. Grip bar wide with arms extended above shoulders.",
							dos = listOf("Pull bar to chin level, leading with elbows", "Squeeze shoulder blades together at bottom", "Keep torso stable with slight lean if needed"),
							donts = listOf("Don't pull behind the neck", "Don't rock or use momentum", "Don't let bar crash into stack"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Dumbbell Shrugs",
							targetMuscles = "Trapezius (Upper Traps)",
							sets = 3,
							reps = 15,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand tall with feet shoulder-width and dumbbells at sides. Keep spine neutral before shrugging shoulders.",
							dos = listOf("Raise shoulders straight up toward ears", "Pause and squeeze traps at top", "Lower slowly under control"),
							donts = listOf("Don't roll or circle shoulders", "Don't jerk or bounce weight at top", "Don't jut head forward"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Dumbbell Bicep Curl",
							targetMuscles = "Biceps Brachii",
							sets = 3,
							reps = 15,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand with feet hip-width, dumbbells at sides, palms forward. Keep torso upright and core engaged.",
							dos = listOf("Curl dumbbells up by flexing elbows", "Keep elbows close to torso until near top", "Perform slow and controlled reps"),
							donts = listOf("Don't use momentum or swing body", "Don't lift elbows away from torso", "Don't neglect the lowering phase"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Back Extension",
							targetMuscles = "Lower Back (Erector Spinae), Glutes, Hamstrings",
							sets = 3,
							reps = 15,
							weightGuidance = "Start with bodyweight or light resistance for 10–15 reps, focusing on control.",
							properPosture = "Position legs on bench pad at hips. With hands behind head or crossed on chest, hinge at hips and lower torso down.",
							dos = listOf("Lift torso by contracting lower back and glutes", "Keep spine neutral throughout", "Pause at top before lowering"),
							donts = listOf("Don't hyperextend past neutral", "Don't jerk or bounce movement", "Don't let hips rise off pad"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Farmer Walk (2 Rounds)",
							targetMuscles = "Legs (Quads, Hamstrings, Calves), Core (Abs, Lower Back), Shoulders (Traps, Forearms)",
							sets = 2,
							reps = 20,
							weightGuidance = "Hold heavy dumbbells or kettlebells at sides; start with a manageable weight and focus on posture.",
							properPosture = "Stand tall with shoulders back and down, chest up. Engage core and hold weights firmly at sides. Maintain neutral spine.",
							dos = listOf("Walk with small, controlled steps", "Keep core braced and shoulders stable", "Breathe normally throughout walk"),
							donts = listOf("Don't lean to one side or overstride", "Don't let shoulders shrug up", "Don't rush – maintain steady pace"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						)
					),
					"Wednesday" to listOf(
						Exercise(
							exerciseName = "Treadmill (Cardio)",
							targetMuscles = "Cardiovascular system (heart, lungs) and Legs",
							sets = 1,
							reps = 30,
							weightGuidance = "Perform 30–60 minutes at moderate intensity (e.g., brisk walk or light jog).",
							properPosture = "Run or walk with upright posture, shoulders relaxed, head facing forward.",
							dos = listOf("Maintain steady pace", "Use arms naturally to assist movement"),
							donts = listOf("Don't hang on to the handles excessively", "Don't hunch shoulders"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Elliptical (Cardio)",
							targetMuscles = "Cardiovascular system and Legs",
							sets = 1,
							reps = 30,
							weightGuidance = "Perform 30–60 minutes at moderate resistance and steady pace.",
							properPosture = "Stand upright, lightly hold handles, keep core engaged.",
							dos = listOf("Use full range of motion", "Keep legs and arms moving smoothly"),
							donts = listOf("Don't lean heavily on handles", "Don't lock knees"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Cycling (Cardio)",
							targetMuscles = "Cardiovascular system and Legs (Quads, Hamstrings, Calves)",
							sets = 1,
							reps = 30,
							weightGuidance = "Perform 30–60 minutes at a steady pace and moderate resistance.",
							properPosture = "Sit with a slight forward lean from hips, hands on handlebars, maintain a stable back.",
							dos = listOf("Keep cadence consistent", "Adjust seat height for a slight knee bend at bottom of pedal stroke"),
							donts = listOf("Don't slouch forward", "Don't pedal with locked knees"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Skipping Rope (Cardio)",
							targetMuscles = "Cardiovascular system and Legs (Calves)",
							sets = 1,
							reps = 30,
							weightGuidance = "Perform 30–60 minutes at a consistent rhythm.",
							properPosture = "Keep head up, gaze forward, shoulders relaxed. Land softly on balls of feet.",
							dos = listOf("Rotate wrists to swing rope", "Jump from ankles, keeping knees slightly bent"),
							donts = listOf("Don't hunch shoulders", "Don't land flat-footed"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Forearm Crunch",
							targetMuscles = "Rectus Abdominis",
							sets = 3,
							reps = 15,
							weightGuidance = "Focus on form; no external weight needed.",
							properPosture = "Lie on back with forearms resting by ears or across chest. Keep chin slightly tucked.",
							dos = listOf("Lift shoulder blades off ground by engaging abs", "Exhale as you crunch up"),
							donts = listOf("Don't pull on your neck", "Don't use momentum to lift"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Leg Raises",
							targetMuscles = "Lower Abdominals, Hip Flexors",
							sets = 3,
							reps = 12,
							weightGuidance = "Focus on controlled movement; no weight used.",
							properPosture = "Lie on back with legs straight; place hands under hips for support.",
							dos = listOf("Lift legs up to 90° with control", "Keep lower back pressed to floor"),
							donts = listOf("Don't arch lower back", "Don't swing legs"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Lateral Flexion (Side Bends)",
							targetMuscles = "Obliques (Side Abdominals)",
							sets = 3,
							reps = 15,
							weightGuidance = "Use light dumbbell or bodyweight; keep movement slow and controlled.",
							properPosture = "Stand with feet hip-width. Place one hand behind head or hold weight at side.",
							dos = listOf("Bend torso to side, engaging oblique", "Return slowly to upright position"),
							donts = listOf("Don't lean forward or backward", "Don't swing torso"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Plank",
							targetMuscles = "Core (Transverse Abdominis, Rectus Abdominis, Obliques, Lower Back)",
							sets = 3,
							reps = 60,
							weightGuidance = "Hold position for 30–60 seconds per set.",
							properPosture = "Forearms on floor, elbows under shoulders. Keep body in a straight line from head to heels.",
							dos = listOf("Keep core engaged and hips level", "Breathe steadily"),
							donts = listOf("Don't let hips sag or pike", "Don't arch back or drop shoulders"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						)
					),
					"Thursday" to listOf(
						Exercise(
							exerciseName = "Squat",
							targetMuscles = "Quadriceps, Glutes, Hamstrings",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand with feet shoulder-width apart and toes slightly turned out. Keep chest up, back straight, core tight. Sit back into hips as you bend knees.",
							dos = listOf("Drive through heels", "Push knees out in line with toes", "Keep chest up and gaze forward"),
							donts = listOf("Let knees collapse inward", "Round or arch the lower back", "Lean too far forward"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Lunge",
							targetMuscles = "Quadriceps, Hamstrings, Glutes, Adductors",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Step forward with one leg, keeping torso upright. Bend both knees until front knee is ~90° and shin vertical. Keep back straight and core engaged.",
							dos = listOf("Keep front shin vertical", "Push knee outward to avoid knee collapse", "Maintain an upright chest"),
							donts = listOf("Don't lean forward at waist", "Don't let lunging knee go past toes", "Don't add weight until form is solid"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Leg Extension",
							targetMuscles = "Quadriceps",
							sets = 3,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit with back against pad and adjust seat so knees align with machine pivot. Position feet under padded bar.",
							dos = listOf("Extend legs fully to contract quads", "Control the weight on the way down", "Sit upright with hips on seat"),
							donts = listOf("Don't lock knees out forcefully", "Don't lift hips off seat", "Don't use momentum"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Standing Calf Raise",
							targetMuscles = "Calves (Gastrocnemius, Soleus)",
							sets = 3,
							reps = 15,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand on a raised platform with balls of feet, heels hanging. Keep knees slightly bent and core engaged.",
							dos = listOf("Push through balls of feet to raise heels", "Hold top contraction briefly", "Use slow, controlled movement"),
							donts = listOf("Don't bounce or use momentum", "Don't lean forward or backward", "Don't lock knees"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Seated Calf Raise",
							targetMuscles = "Calves (Soleus focus)",
							sets = 3,
							reps = 15,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit with knees under pads and balls of feet on platform. Keep back straight and core engaged.",
							dos = listOf("Extend ankles fully and pause at top", "Lower heels below platform for full stretch", "Maintain steady, controlled tempo"),
							donts = listOf("Don't bounce at bottom or top", "Don't lift thighs off pads", "Don't rush reps"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Leg Curl",
							targetMuscles = "Hamstrings (and calves)",
							sets = 3,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Lie face down on bench with padded lever just above heels. Keep hips down and torso flat on bench.",
							dos = listOf("Curl heels toward glutes to fully flex hamstrings", "Keep hips pressed down on bench", "Return to full stretch with control"),
							donts = listOf("Don't lift hips or arch back during movement", "Don't use excessive weight causing jerking", "Don't drop weight quickly"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						)
					),
					"Friday" to listOf(
						Exercise(
							exerciseName = "Seated Cable Row Machine",
							targetMuscles = "Pectoralis Major, Anterior Deltoids, Triceps",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Lie back on bench with feet flat on floor. Grip bar slightly wider than shoulders, engage core and glutes.",
							dos = listOf("Keep elbows at ~45° from body", "Drive feet into floor for stability", "Lower bar to nipple level under control"),
							donts = listOf("Don't flare elbows wide", "Don't bounce bar off chest", "Don't arch back excessively"),
							tutorialVideoUrl = "https://youtube.com/shorts/WbCEvFA0NJs?si=K62zk7YIYkBAOnWk"
						),
						Exercise(
							exerciseName = "Overhead Press",
							targetMuscles = "Deltoids (Shoulders), Triceps",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand with feet hip-width. Hold barbell at collarbone, brace core and glutes. Press bar overhead in a straight path.",
							dos = listOf("Keep elbows under wrists and in front of body", "Engage abs and glutes throughout", "Exhale as you press up"),
							donts = listOf("Don't arch lower back (keep core tight)", "Don't flare elbows too far out", "Don't bend head excessively backward"),
							tutorialVideoUrl = "https://youtube.com/shorts/k6tzKisR3NY?si=YdymXVyLGAaE3n5N"
						),
						Exercise(
							exerciseName = "Low Cable Row",
							targetMuscles = "Upper and Middle Back (Lats, Rhomboids, Trapezius), Biceps",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit upright with knees slightly bent, chest out, and feet against platform. Grasp handle with arms extended.",
							dos = listOf("Pull handle to lower abdomen while squeezing shoulder blades", "Keep back straight and chest up", "Use arms and back muscles, not momentum"),
							donts = listOf("Don't round the lower back", "Don't lean back excessively", "Don't use torso swing to lift weight"),
							tutorialVideoUrl = "https://youtube.com/shorts/qD1WZ5pSuvk?si=XtZtqOL_T8Lfr6iE"
						),
						Exercise(
							exerciseName = "Lat Pulldown",
							targetMuscles = "Latissimus Dorsi, Teres Major (Back), Biceps",
							sets = 4,
							reps = 12,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Sit with thighs secured under pad, lean back slightly. Grip bar wide with arms extended above shoulders.",
							dos = listOf("Pull bar to chin level, leading with elbows", "Squeeze shoulder blades together at bottom", "Keep torso stable with slight lean if needed"),
							donts = listOf("Don't pull behind the neck", "Don't rock or use momentum", "Don't let bar crash into stack"),
							tutorialVideoUrl = "https://youtube.com/shorts/5s6KGLTMgoI?si=dPqMwgIfGWOvgWBc"
						),
						Exercise(
							exerciseName = "Dumbbell Shrugs",
							targetMuscles = "Trapezius (Upper Traps)",
							sets = 3,
							reps = 15,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand tall with feet shoulder-width and dumbbells at sides. Keep spine neutral before shrugging shoulders.",
							dos = listOf("Raise shoulders straight up toward ears", "Pause and squeeze traps at top", "Lower slowly under control"),
							donts = listOf("Don't roll or circle shoulders", "Don't jerk or bounce weight at top", "Don't jut head forward"),
							tutorialVideoUrl = "https://youtube.com/shorts/j2-RccWDhDo?si=hwJU99DGUrdK67fM"
						),
						Exercise(
							exerciseName = "Dumbbell Bicep Curl",
							targetMuscles = "Biceps Brachii",
							sets = 3,
							reps = 15,
							weightGuidance = "Start with a weight that allows 10–15 reps with good form, challenging last 2 reps.",
							properPosture = "Stand with feet hip-width, dumbbells at sides, palms forward. Keep torso upright and core engaged.",
							dos = listOf("Curl dumbbells up by flexing elbows", "Keep elbows close to torso until near top", "Perform slow and controlled reps"),
							donts = listOf("Don't use momentum or swing body", "Don't lift elbows away from torso", "Don't neglect the lowering phase"),
							tutorialVideoUrl = "https://youtube.com/shorts/_aoad2yuP5w?si=I-RofV3HIapIhJ1d"
						),
						Exercise(
							exerciseName = "Back Extension",
							targetMuscles = "Lower Back (Erector Spinae), Glutes, Hamstrings",
							sets = 3,
							reps = 15,
							weightGuidance = "Start with bodyweight or light resistance for 10–15 reps, focusing on control.",
							properPosture = "Position legs on bench pad at hips. With hands behind head or crossed on chest, hinge at hips and lower torso down.",
							dos = listOf("Lift torso by contracting lower back and glutes", "Keep spine neutral throughout", "Pause at top before lowering"),
							donts = listOf("Don't hyperextend past neutral", "Don't jerk or bounce movement", "Don't let hips rise off pad"),
							tutorialVideoUrl = "https://youtube.com/shorts/Wpreb69h2fE?si=zTY8ZKRO-uU6ym-W"
						),
						Exercise(
							exerciseName = "Farmer Walk (2 Rounds)",
							targetMuscles = "Legs (Quads, Hamstrings, Calves), Core (Abs, Lower Back), Shoulders (Traps, Forearms)",
							sets = 2,
							reps = 20,
							weightGuidance = "Hold heavy dumbbells or kettlebells at sides; start with a manageable weight and focus on posture.",
							properPosture = "Stand tall with shoulders back and down, chest up. Engage core and hold weights firmly at sides. Maintain neutral spine.",
							dos = listOf("Walk with small, controlled steps", "Keep core braced and shoulders stable", "Breathe normally throughout walk"),
							donts = listOf("Don't lean to one side or overstride", "Don't let shoulders shrug up", "Don't rush – maintain steady pace"),
							tutorialVideoUrl = "https://youtube.com/shorts/6j4mc-3Ze50?si=PUgjczY-dfScnvnX"
						)
					),
					"Saturday" to listOf(
						Exercise(
							exerciseName = "Treadmill (Cardio)",
							targetMuscles = "Cardiovascular system (heart, lungs) and Legs",
							sets = 1,
							reps = 30,
							weightGuidance = "Perform 30–60 minutes at moderate intensity (e.g., brisk walk or light jog).",
							properPosture = "Run or walk with upright posture, shoulders relaxed, head facing forward.",
							dos = listOf("Maintain steady pace", "Use arms naturally to assist movement"),
							donts = listOf("Don't hang on to the handles excessively", "Don't hunch shoulders"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Elliptical (Cardio)",
							targetMuscles = "Cardiovascular system and Legs",
							sets = 1,
							reps = 30,
							weightGuidance = "Perform 30–60 minutes at moderate resistance and steady pace.",
							properPosture = "Stand upright, lightly hold handles, keep core engaged.",
							dos = listOf("Use full range of motion", "Keep legs and arms moving smoothly"),
							donts = listOf("Don't lean heavily on handles", "Don't lock knees"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Cycling (Cardio)",
							targetMuscles = "Cardiovascular system and Legs (Quads, Hamstrings, Calves)",
							sets = 1,
							reps = 30,
							weightGuidance = "Perform 30–60 minutes at a steady pace and moderate resistance.",
							properPosture = "Sit with a slight forward lean from hips, hands on handlebars, maintain a stable back.",
							dos = listOf("Keep cadence consistent", "Adjust seat height for a slight knee bend at bottom of pedal stroke"),
							donts = listOf("Don't slouch forward", "Don't pedal with locked knees"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Skipping Rope (Cardio)",
							targetMuscles = "Cardiovascular system and Legs (Calves)",
							sets = 1,
							reps = 30,
							weightGuidance = "Perform 30–60 minutes at a consistent rhythm.",
							properPosture = "Keep head up, gaze forward, shoulders relaxed. Land softly on balls of feet.",
							dos = listOf("Rotate wrists to swing rope", "Jump from ankles, keeping knees slightly bent"),
							donts = listOf("Don't hunch shoulders", "Don't land flat-footed"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Forearm Crunch",
							targetMuscles = "Rectus Abdominis",
							sets = 3,
							reps = 15,
							weightGuidance = "Focus on form; no external weight needed.",
							properPosture = "Lie on back with forearms resting by ears or across chest. Keep chin slightly tucked.",
							dos = listOf("Lift shoulder blades off ground by engaging abs", "Exhale as you crunch up"),
							donts = listOf("Don't pull on your neck", "Don't use momentum to lift"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Leg Raises",
							targetMuscles = "Lower Abdominals, Hip Flexors",
							sets = 3,
							reps = 12,
							weightGuidance = "Focus on controlled movement; no weight used.",
							properPosture = "Lie on back with legs straight; place hands under hips for support.",
							dos = listOf("Lift legs up to 90° with control", "Keep lower back pressed to floor"),
							donts = listOf("Don't arch lower back", "Don't swing legs"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Lateral Flexion (Side Bends)",
							targetMuscles = "Obliques (Side Abdominals)",
							sets = 3,
							reps = 15,
							weightGuidance = "Use light dumbbell or bodyweight; keep movement slow and controlled.",
							properPosture = "Stand with feet hip-width. Place one hand behind head or hold weight at side.",
							dos = listOf("Bend torso to side, engaging oblique", "Return slowly to upright position"),
							donts = listOf("Don't lean forward or backward", "Don't swing torso"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						),
						Exercise(
							exerciseName = "Plank",
							targetMuscles = "Core (Transverse Abdominis, Rectus Abdominis, Obliques, Lower Back)",
							sets = 3,
							reps = 60,
							weightGuidance = "Hold position for 30–60 seconds per set.",
							properPosture = "Forearms on floor, elbows under shoulders. Keep body in a straight line from head to heels.",
							dos = listOf("Keep core engaged and hips level", "Breathe steadily"),
							donts = listOf("Don't let hips sag or pike", "Don't arch back or drop shoulders"),
							tutorialVideoUrl = "PLACEHOLDER_VIDEO_URL"
						)
					),
					"Sunday" to emptyList() // Rest day
				)
			)
		}
	}
}

// WorkoutRepository has been moved to a separate file