## LibGDX Ashley ECS Example

Minimal project with uses [ashley](https://github.com/libgdx/ashley/wiki) to manage some different entites, control systems (ai and user controlled) and has a tiled map system that manages collision detection with tiles.

note: you're going to want to run the desktop launcher and use the keyboard - I didn't implement android controls.

The idea behind this is to help illustrate why you'd want to use an ECS - if you want to make it so 'alex' is ai controlled and the enemy character is user controlled, just add the desired component with each entity. If you want the camera to follow the enemy instead of the player, just add the camera follow component to the enemy instead of the player.

The engine assists in exposing certain types of entities to the systems that interact with them - just call Family.all and list the component classes you need. In the MapSystem, I want horizontal collision to change the direction of the ai controlled entities that have a physics component, but not the player. This is how to get those collections.

	player = engine.getEntitiesFor(Family.all(PhysicsComponent::class.java, UserControlledComponent::class.java).get())
	ai = engine.getEntitiesFor(Family.all(PhysicsComponent::class.java, AiControlledComponent::class.java).get())

For more info about the justification for using this programming pattern, check my write-up on [my website](https://jojomickymack.gitlab.io/reverie/post/ashley_ecs/).
