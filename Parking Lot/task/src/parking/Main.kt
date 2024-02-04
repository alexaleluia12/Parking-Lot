package parking

const val NOT_FOUND_PARK = -1

data class CarSlot(
    val position: Int = NOT_FOUND_PARK,
    val registration: String = "Not-def",
    val color: String = "unknown")

class Parking() {
    var space: Int = 0
    var isAvailable = false
    lateinit var slots: MutableList<CarSlot?>

    fun create(space: Int) {
        slots = MutableList(space) { null }
        this.space = space
        isAvailable = true
        println("Created a parking lot with $space spots.")
    }

    fun status() {
        if (!isAvailable) {
            println("Sorry, a parking lot has not been created.")
            return
        }
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
    fun park(color: String, registration: String) {
        if (!isAvailable) {
            println("Sorry, a parking lot has not been created.")
            return
        }
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

    fun leave(position: Int) {
        if (!isAvailable) {
            println("Sorry, a parking lot has not been created.")
            return
        }
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

    fun showSpotByColor(color: String) {
        if (!isAvailable) {
            println("Sorry, a parking lot has not been created.")
            return
        }

        val mach = mutableListOf<Int>()
        for (index in slots.indices) {
            val carSpot = slots[index]
            carSpot?.let {
                if (it.color.equals(color, ignoreCase = true)) {
                    mach.add(index + 1)
                }
            }
        }
        if (mach.isNotEmpty()) {
            println(mach.joinToString(separator = ", "))
        } else {
            println("No cars with color $color were found.")
        }
    }

    fun showSpotByRegistration(registration: String) {
        if (!isAvailable) {
            println("Sorry, a parking lot has not been created.")
            return
        }

        val mach = mutableListOf<Int>()
        for (index in slots.indices) {
            val carSpot = slots[index]
            carSpot?.let {
                if (it.registration.equals(registration, ignoreCase = true)) {
                    mach.add(index + 1)
                }
            }
        }
        if (mach.isNotEmpty()) {
            println(mach.joinToString(separator = ", "))
        } else {
            println("No cars with registration number $registration were found.")
        }
    }


    fun showRegisterByColor(color: String) {
        if (!isAvailable) {
            println("Sorry, a parking lot has not been created.")
            return
        }

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

    fun parkMessage(color: String, position: Int) {
        println("$color car parked in spot $position.")
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
