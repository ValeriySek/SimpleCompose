package com.example.simplecompose.compose_galaxy

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.example.simplecompose.compose_galaxy.data.PlanetData
import com.example.simplecompose.compose_galaxy.data.StarData
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun Galaxy1(
    modifier: Modifier
) {
    val animationTargetState = remember { mutableStateOf(0f) }

    val animatedFloatState = animateFloatAsState(
        targetValue = animationTargetState.value,
        animationSpec = tween(durationMillis = 3000)
    )

    Canvas(
        modifier = modifier
            .clickable {
                animationTargetState.value = 1f
            }
    ) {
        drawCircle(
            color = Color.White,
            radius = 50f,
            alpha = animatedFloatState.value
        )
    }
}


@Composable
fun Galaxy2(
    modifier: Modifier
) {
    val animationTargetState = remember { mutableStateOf(AnimationState.START) }

    val transition = updateTransition(targetState = animationTargetState.value, label = "")

    val circleAlpha = transition.animateFloat(
        transitionSpec = { tween(durationMillis = 3000)}, label = ""
    ) {
        if(it == AnimationState.START) 0f else 1f
    }

    val circleRadius = transition.animateFloat(
        transitionSpec = { tween(durationMillis = 3000)}, label = ""
    ) {
        if(it == AnimationState.START) 10f else 50f
    }

    Canvas(
        modifier = modifier
    ) {
        drawCircle(
            color = Color.White,
            radius = circleRadius.value,
            alpha = circleAlpha.value
        )

        animationTargetState.value = AnimationState.END
    }
}


@Composable
fun GalaxyInfinite(
    modifier: Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()

    val infiniteAnimatedFloat = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(),
            repeatMode = RepeatMode.Reverse
        )
    )


    Canvas(
        modifier = modifier
    ) {
        drawCircle(
            color = Color.White,
            radius = 50f,
            alpha = infiniteAnimatedFloat.value
        )
    }
}

enum class AnimationState {
    START,
    RUNNING,
    PAUSED,
    END
}


@Composable
fun GalaxyAnimatable(
    modifier: Modifier
) {
    val animationScope = rememberCoroutineScope()

    val animatableX = remember { Animatable(initialValue = 0f) }
    val animatableY = remember { Animatable(initialValue = 0f) }

    Canvas(
        modifier = modifier
            .clickable {
                animationScope.launch {
                    launch {
                        animatableX.animateTo(
                            targetValue = (0..500)
                                .random().toFloat(),
                            animationSpec = tween(durationMillis = 1000)
                        )
                    }

                    launch {
                        animatableY.animateTo(
                            targetValue = (0..1000)
                                .random().toFloat(),
                            animationSpec = tween(durationMillis = 1000)
                        )
                    }
                }
            }
    ) {
        drawCircle(
            color = Color.White,
            radius = 50f,
            center = Offset(
                x = animatableX.value,
                y = animatableY.value
            )
        )
    }
}


@Composable
fun GalaxyTargetBased(
    modifier: Modifier
) {
    val targetBasedAnimation = remember{
        TargetBasedAnimation(
            animationSpec = tween(2000),
            typeConverter = Float.VectorConverter,
            initialValue = 0f,
            targetValue = 500f
        )
    }

    var playTime = remember{ 0L }

    val animationScope = rememberCoroutineScope()

    var animationState = remember {
        mutableStateOf(AnimationState.PAUSED)
    }

    var animationValue = remember {
        mutableStateOf(0f)
    }

    val onClick: () -> Unit = {
        animationState.value = when(animationState.value) {
            AnimationState.PAUSED -> AnimationState.RUNNING
            AnimationState.RUNNING -> AnimationState.PAUSED
            else -> AnimationState.END
        }

        animationScope.launch {
            val startTime = withFrameNanos { it } - playTime

            while (animationState.value == AnimationState.RUNNING) {
                playTime = withFrameNanos { it } - startTime
                animationValue.value = targetBasedAnimation.getValueFromNanos(playTime)
            }
        }
    }

    Canvas(
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        drawCircle(
            color = Color.White,
            radius = 50f,
            center = Offset(
                x = animationValue.value,
                y = animationValue.value
            )
        )
    }
}


@Composable
fun GalaxyDecayAnimation(
    modifier: Modifier
) {
    val decayAnimation = remember{
        DecayAnimation(
            animationSpec = FloatExponentialDecaySpec(
                frictionMultiplier = 0.7f
            ),
            initialValue = 0f,
            initialVelocity = 500f
        )
    }

    var playTime = remember{ 0L }

    val animationScope = rememberCoroutineScope()

    val animationState = remember { mutableStateOf(AnimationState.PAUSED) }
    val animationValue = remember { mutableStateOf(0f) }

    val onClick: () -> Unit = {
        animationState.value = when(animationState.value) {
            AnimationState.PAUSED -> AnimationState.RUNNING
            AnimationState.RUNNING -> AnimationState.PAUSED
            else -> AnimationState.END
        }

        animationScope.launch {
            var startTime = withFrameNanos { it } - playTime

            while (animationState.value == AnimationState.RUNNING) {

                if(decayAnimation.isFinishedFromNanos(playTime)) startTime = withFrameNanos { it }

                playTime = withFrameNanos { it } - startTime
                animationValue.value = decayAnimation.getValueFromNanos(playTime)
            }
        }
    }

    Canvas(
        modifier = modifier
//            .offset(animationValue.value.dp,
//            250.dp)
            .clickable(onClick = onClick)
    ) {
        drawCircle(
            color = Color.White,
            radius = 50f,
            center = Offset(
                x = animationValue.value,
                y = 250f
            )
        )
    }
}











@ExperimentalStdlibApi
@Composable
fun Galaxy(
    modifier: Modifier = Modifier,
    planetData: PlanetData = PlanetData(),
    starData: StarData = StarData()
) {
    val planetRandomizers = remember {
        generateRandomPlanetDataset(
            planetData = planetData
        )
    }

    val starRandomizers = remember {
        generateRandomStarDataset(
            starData = starData
        )
    }

    val infiniteTransition = rememberInfiniteTransition()

    val planetShiftAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = planetData.planetAnimationSpec,
            repeatMode = RepeatMode.Reverse
        )
    )

    val starAlphaAnimation = infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = starData.starShiningAnimationSpec,
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = modifier,
        onDraw = {
            drawGalaxy(
                drawScope = this,
                planetRandomizers = planetRandomizers,
                planetShiftAnimationValue = planetShiftAnimation.value,
                starRandomizers = starRandomizers,
                starAlphaAnimationValue = starAlphaAnimation.value
            )
        }
    )
}

private fun drawGalaxy(
    drawScope: DrawScope,
    planetRandomizers: List<PlanetRandomizer>,
    planetShiftAnimationValue: Float,
    starRandomizers: List<StarRandomizer>,
    starAlphaAnimationValue: Float
) {
    val diagonal = hypot(drawScope.size.width, drawScope.size.height)

    // Draw all planets
    planetRandomizers.forEachIndexed { index, planetRandomizer ->
        drawPlanet(
            radius = planetRandomizer.radius,
            center = getRandomPointOutsideGalaxy(
                drawScope = drawScope,
                planetCoefficientsData = planetRandomizer.planetCoefficientsData,
                shiftAnimationValue = planetShiftAnimationValue,
                diagonal = diagonal,
                // Half is starting from reverse the other half is not
                isStaringFromReverse = index < (planetRandomizers.size / 2)
            ),
            color = planetRandomizer.color,
            alpha = planetRandomizer.alpha,
            drawScope = drawScope
        )
    }

    // Draw all stars
    starRandomizers.forEach {
        drawStar(
            drawScope = drawScope,
            sideLength = it.starCoefficientsData.sideLengthCoefficient * 7f,
            numberOfEdges = (it.starCoefficientsData.edgeNumberCoefficient * 10).toInt(),
            interiorAngle = it.starCoefficientsData.interiorAngleCoefficient * 90f,
            startOffset = Offset(
                x = it.starCoefficientsData.startPointXCoefficient * drawScope.size.width,
                y = it.starCoefficientsData.startPointYCoefficient * drawScope.size.height
            ),
            fillColor = it.color,
            alpha = it.starCoefficientsData.shiningAnimationCoefficient * starAlphaAnimationValue
        )
    }
}

private fun drawPlanet(
    radius: Float,
    center: Offset,
    color: Color,
    alpha: Float,
    drawScope: DrawScope
) {
    drawScope.drawCircle(
        color = color,
        radius = radius,
        center = center,
        alpha = alpha
    )
}

private fun drawStar(
    drawScope: DrawScope,
    sideLength: Float,
    numberOfEdges: Int,
    interiorAngle: Float,
    startOffset: Offset,
    fillColor: Color,
    alpha: Float
) {
    // Move to the start offset
    val starPath = Path().also {
        it.moveTo(startOffset.x, startOffset.y)
    }

    // Interior edge angle is the angle of the other edges
    // in the isosceles triangle of the end points of the star
    val starInteriorEdgeAngle = (180.0 - interiorAngle) / 2
    val starExteriorAngle = 360 - starInteriorEdgeAngle

    var firstPoint = Offset(
        x = startOffset.x,
        y = startOffset.y
    )

    for (i in (1..numberOfEdges)) {
        val lineRotationAngle = i * 360.0 / numberOfEdges

        val secondPointRotationAngleInRadians = Math.toRadians(
            starExteriorAngle + lineRotationAngle
        ).toFloat()

        val secondPoint = Offset(
            x = firstPoint.x + sideLength * sin(secondPointRotationAngleInRadians),
            y = firstPoint.y + sideLength * cos(secondPointRotationAngleInRadians)
        )

        val thirdPointRotationAngleInRadians = Math.toRadians(
            starInteriorEdgeAngle + lineRotationAngle
        ).toFloat()

        starPath.lineTo(
            x = secondPoint.x,
            y = secondPoint.y
        )

        val thirdPoint = Offset(
            x = secondPoint.x + sideLength * sin(thirdPointRotationAngleInRadians),
            y = secondPoint.y + sideLength * cos(thirdPointRotationAngleInRadians)
        )

        starPath.lineTo(
            x = thirdPoint.x,
            y = thirdPoint.y
        )

        firstPoint = thirdPoint
    }

    drawScope.drawPath(
        path = starPath,
        color = fillColor,
        alpha = alpha
    )
}

private fun getRandomPointOutsideGalaxy(
    drawScope: DrawScope,
    planetCoefficientsData: PlanetCoefficientsData,
    shiftAnimationValue: Float,
    diagonal: Float,
    isStaringFromReverse: Boolean
): Offset {
    // We will first pick a random point inside the screen and
    // move it outside of the screen by adding a factor of diagonal
    val randomXInGalaxy = planetCoefficientsData.coefficientX * drawScope.size.width
    val randomYInGalaxy = planetCoefficientsData.coefficientY * drawScope.size.height

    val (shiftValueX, shiftValueY) = if (isStaringFromReverse) {
        // Animate from the end point to the starting point
        Pair(
            diagonal * (shiftAnimationValue - 1) * -sin(planetCoefficientsData.shiftAngle),
            diagonal * (shiftAnimationValue - 1) * -cos(planetCoefficientsData.shiftAngle)
        )
    } else {
        // Animate from the starting point to the end point
        Pair(
            diagonal * shiftAnimationValue * sin(planetCoefficientsData.shiftAngle),
            diagonal * shiftAnimationValue * cos(planetCoefficientsData.shiftAngle)
        )
    }

    return Offset(
        x = randomXInGalaxy + shiftValueX,
        y = randomYInGalaxy + shiftValueY
    )
}

@ExperimentalStdlibApi
private fun generateRandomPlanetDataset(
    planetData: PlanetData
): List<PlanetRandomizer> {
    return buildList {
        for (i in 0..planetData.numberOfPlanet) {
            add(PlanetRandomizer(planetData = planetData))
        }
    }
}

private fun generateRandomAngle(range: IntRange): Float {
    return Math.toRadians(
        range.random().toDouble()
    ).toFloat()
}

@ExperimentalStdlibApi
private fun generateRandomStarDataset(
    starData: StarData
): List<StarRandomizer> {
    return buildList {
        for (i in (0 until starData.numberOfStars)) {
            add(StarRandomizer(starData = starData))
        }
    }
}


/**
 * Keeps values for randomization
 */
data class PlanetRandomizer(
    private val planetData: PlanetData
) {
    val planetCoefficientsData: PlanetCoefficientsData = generateRandomCoefficients()
    val radius: Float = getRandomPlanetRadius(planetData.maxPlanetRadius)
    val alpha: Float = getRandomPlanetAlpha(planetData.maxPlanetAlpha)
    val color: Color = getRandomPlanetColor(planetData.planetColors)

    private fun getRandomPlanetRadius(
        maxRadius: Float
    ): Float {
        return (0..100).random() / 100f * maxRadius
    }

    private fun getRandomPlanetAlpha(
        maxAlpha: Float
    ): Float {
        return (0..100).random() / 100f * maxAlpha
    }

    private fun getRandomPlanetColor(planetColors: List<Color>) = planetColors.random()

    private fun generateRandomCoefficients(): PlanetCoefficientsData {

        // These coefficients will be randomly selected for x and y
        // For instance if coefficient x is the first one then coefficient
        // for the y will be second one. This is to generate random offsets
        // which is maxPlanetRadius distance away from the screen edge
        val firstRandomCoefficient = (0..100).random() / 100f

        // Shifting coefficients by 0.1f will make the planet itself will not
        // be visible at the starting point which will be drawn outside of the
        // screen
        val secondRandomCoefficient = listOf(
            0f - 0.1f,
            1f + 0.1f
        ).random()

        val shuffledCoefficientList = listOf(
            firstRandomCoefficient,
            secondRandomCoefficient
        ).shuffled()

        val coefficientX = shuffledCoefficientList.first()
        val coefficientY = shuffledCoefficientList.last()

        val shiftCoefficient = generateRandomAngleForShiftCoefficient(
            coefficientX = coefficientX,
            coefficientY = coefficientY
        )

        return PlanetCoefficientsData(
            coefficientX = coefficientX,
            coefficientY = coefficientY,
            shiftAngle = shiftCoefficient
        )
    }

    /**
     * Shift coefficient will be used to scale the shift value used to move the planet.
     * It is important to move the planet (which will firstly be placed outside of the screen)
     * towards the screen.
     */
    private fun generateRandomAngleForShiftCoefficient(
        coefficientX: Float,
        coefficientY: Float
    ): Float {
        // Random angle (0, PI)
        val randomAngle = generateRandomAngle(range = (0..180))

        return when {
            coefficientX > 1f -> {
                // Bottom to the bottom of the screen

                // Rotate by 180deg
                randomAngle + 180
            }
            coefficientX < 0f -> {
                // Top to the top of the screen

                // No need any rotation
                randomAngle
            }
            coefficientY > 1f -> {
                // Right to the right of the screen

                // Rotate by 90deg
                randomAngle + 90
            }
            coefficientY < 0f -> {
                // Left to the left of the screen

                // Rotate by -90deg
                randomAngle - 90
            }
            else -> throw IllegalStateException(
                "One of the coefficients must satify the " +
                        "above conditions"
            )
        }
    }
}

data class PlanetCoefficientsData(
    val coefficientX: Float,
    val coefficientY: Float,
    val shiftAngle: Float
)

data class StarRandomizer(
    private val starData: StarData
) {
    // All coefficients are between (0f, 1f)
    val starCoefficientsData = StarCoefficientsData(
        startPointXCoefficient = (0..100).random() / 100f,
        startPointYCoefficient = (0..100).random() / 100f,
        sideLengthCoefficient = (0..100).random() / 100f,
        edgeNumberCoefficient = (0..100).random() / 100f,
        interiorAngleCoefficient = (0..100).random() / 100f,
        shiningAnimationCoefficient = (0..100).random() / 100f
    )

    val color = starData.starColors.random()
}

data class StarCoefficientsData(
    val startPointXCoefficient: Float,
    val startPointYCoefficient: Float,
    val sideLengthCoefficient: Float,
    val edgeNumberCoefficient: Float,
    val interiorAngleCoefficient: Float,
    val shiningAnimationCoefficient: Float
)
