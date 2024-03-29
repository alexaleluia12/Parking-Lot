package parking

const val NOT_FOUND_PARK = -1

data class CarSlot(
    val position: Int = NOT_FOUND_PARK,
    val registration: String = "Not-def",
    val color: String = "unknown")

abstract class ParkingABS() {
    var space: Int = 0
    var isAvailable = false
    lateinit var slots: MutableList<CarSlot?>

    fun create(space: Int) {
        slots = MutableList(space) { null }
        this.space = space
        isAvailable = true
        println("Created a parking lot with $space spots.")
    }

    open fun status() {
        var oneLeast = false
        for (i in slots.indices) {
            val car = slots[i]
            car?.let {
                println("${i+1} ${car.registration} ${car.color}")
                oneLeast = true
            }
        }
        if (!oneLeast) {
            println("Parking lot is empty.")
        }

    }

    /**
     * park with the lowest possible number
     */
    open fun park(color: String, registration: String) {
        val positionParked = parkInner(color, registration)
        if (positionParked == NOT_FOUND_PARK) {
            println("Sorry, the parking lot is full.")
        } else {
            parkMessage(color, position = positionParked)
        }
    }

    private fun parkInner(color: String, registration: String): Int {
        var foundIndex = NOT_FOUND_PARK
        for (i in slots.indices) {
            val slot = slots[i]
            if (slot == null) {
                foundIndex = i
                break
            }
        }
        if (foundIndex == NOT_FOUND_PARK) return foundIndex

        return with(slots) {
            set(
                foundIndex,
                CarSlot(position = foundIndex+1, registration = registration, color = color)
            )
            foundIndex + 1
        }
    }

    open fun leave(position: Int) {
        val truePosition = position - 1
        val current: CarSlot?  = slots.getOrNull(truePosition)
        current?.let {
            println("Spot $position is free.")
            slots[truePosition] = null
        }
        if (current == null) {
            println("There is no car in spot $position.")
        }
    }

    private fun genericFindSlotPattern(
        template: String,
        predicate: (CarSlot, ) -> Boolean) {
        val mach = mutableListOf<Int>()

        for (index in slots.indices) {
            val carSpot = slots[index]
            carSpot?.let {
                if (predicate(it)) {
                    mach.add(index + 1)
                }
            }
        }
        if (mach.isNotEmpty()) {
            println(mach.joinToString(separator = ", "))
        } else {
            println("No cars with $template were found.")
        }
    }
    open fun showSpotByColor(color: String) {
        genericFindSlotPattern("color $color")
            { car -> car.color.equals(color, ignoreCase = true) }
    }

    open fun showSpotByRegistration(registration: String) {
        genericFindSlotPattern("registration number $registration")
            {car -> car.registration.equals(registration, ignoreCase = true)}
    }

    open fun showRegisterByColor(color: String) {
        /*
        if there are more conditions to store the register use .genericFindSlotPattern() as an example
        to reduce duplicated code
         */
        val mach = mutableListOf<String>()
        for (carSpot in slots) {
            carSpot?.let {
                if (it.color.equals(color, ignoreCase = true)) {
                    mach.add(it.registration)
                }
            }
        }
        if (mach.isNotEmpty()) {
            println(mach.joinToString(separator = ", " ))
        } else {
            println("No cars with color $color were found.")
        }
    }

    private fun parkMessage(color: String, position: Int) {
        println("$color car parked in spot $position.")
    }
}

class Parking : ParkingABS() {
    private fun conditionToRun(): Boolean {
        if (!isAvailable) {
            println("Sorry, a parking lot has not been created.")
            return false
        }
        return true
    }
    override fun showRegisterByColor(color: String) {
        if (conditionToRun()) {
            super.showRegisterByColor(color)
        }
    }

    override fun leave(position: Int) {
        if (conditionToRun()) {
            super.leave(position)
        }
    }

    override fun status() {
        if (conditionToRun()) {
            super.status()
        }
    }

    override fun showSpotByColor(color: String) {
        if (conditionToRun()) {
            super.showSpotByColor(color)
        }
    }

    override fun showSpotByRegistration(registration: String) {
        if (conditionToRun()) {
            super.showSpotByRegistration(registration)
        }
    }

    override fun park(color: String, registration: String) {
        if (conditionToRun()) {
            super.park(color, registration)
        }
    }
}

fun main() {
    val grandCar = Parking()
    var usrInput = readln().split(" ")
    var command = usrInput.first()

    while (command != "exit") {
        when (command) {
            "create" -> {
                val size = usrInput.last().toInt()
                grandCar.create(size)
            }
            "park" -> {
                val (registration, color) = usrInput.slice( 1 .. usrInput.lastIndex)
                grandCar.park(color, registration)
            }
            "leave" -> {
                val spotToLeave = usrInput.last().toInt()
                grandCar.leave(position = spotToLeave)
            }
            "status" -> grandCar.status()
            "spot_by_reg" -> {
                val registerToLookup = usrInput.last()
                grandCar.showSpotByRegistration(registration = registerToLookup)
            }
            "reg_by_color" -> {
                val colorToLookup = usrInput.last()
                grandCar.showRegisterByColor(color = colorToLookup)
            }
            "spot_by_color" -> {
                val colorToLookup = usrInput.last()
                grandCar.showSpotByColor(color = colorToLookup)
            }
            else -> println("Error - command not expected")
        }
        usrInput = readln().split(" ")
        command = usrInput.first()
    }
}
