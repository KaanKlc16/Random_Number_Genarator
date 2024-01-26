package com.kaank.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kaank.myapp.ui.theme.MyAppTheme
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingWithCustomOptions("Kullanıcı")
                }
            }
        }
    }
}

@Composable
fun GreetingWithCustomOptions(name: String, modifier: Modifier = Modifier) {
    var minRange by remember { mutableStateOf("") }
    var maxRange by remember { mutableStateOf("") }
    var numberOfRandomNumbers by remember { mutableStateOf(1) }
    var allowRepetition by remember { mutableStateOf(true) }
    var randomNumbers by remember { mutableStateOf(listOf<Int>()) }
    var errorText by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "İkonun İçeriği",
                modifier = Modifier.size(100.dp),
                tint = Color.DarkGray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = minRange,
            onValueChange = {
                minRange = it.takeIf { it.isNotEmpty() }?.toIntOrNull()?.toString() ?: ""
            },
            label = { Text("Minimum Değer") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = maxRange,
            onValueChange = {
                maxRange = it.takeIf { it.isNotEmpty() }?.toIntOrNull()?.toString() ?: ""
            },
            label = { Text("Maximum Değer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = numberOfRandomNumbers.toString(),
            onValueChange = { numberOfRandomNumbers = it.toIntOrNull() ?: numberOfRandomNumbers },
            label = { Text("Rastgele Sayıların Sayısı") },
            modifier = Modifier.fillMaxWidth(),

            )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Sayılar Tekrar Edebilsin")
            Checkbox(
                checked = allowRepetition,
                onCheckedChange = { allowRepetition = it }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (minRange.isEmpty() || maxRange.isEmpty() || minRange.toIntOrNull() == null || maxRange.toIntOrNull() == null) {
                errorText = "Lütfen geçerli sayıları giriniz."
            } else {
                errorText = validateInput(minRange.toInt(), maxRange.toInt())
                if (errorText == null) {
                    randomNumbers = generateRandomNumbers(minRange.toInt(), maxRange.toInt(), numberOfRandomNumbers, allowRepetition)
                }
            }
        }) {
            Text("Rastgele Sayı Üret")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (randomNumbers.isNotEmpty()) {
            Text(
                text = buildString {
                    append("Rastgele Sayılar:")
                    randomNumbers.forEachIndexed { index, number ->
                        if (index > 0) {
                            // Add a comma or your desired separator
                            append(", ")
                        }
                        append(number)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        } else {
            Text(
                text = errorText ?: "Belirtilen aralıkta benzersiz sayılar oluşturulamıyor.",
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.error
            )
        }

    }
}

fun validateInput(minValue: Int, maxValue: Int): String? {
    return when {
        minValue >= maxValue -> "Minimum aralık, maksimum aralıktan az olmalıdır."

        else -> null
    }
}

fun generateRandomNumbers(min: Int, max: Int, count: Int, allowRepetition: Boolean): List<Int> {
    val range = if (min < max) min..max else max..min

    return if (allowRepetition) {
        List(count) { Random.nextInt(range) }
    } else {
        if (count > range.count()) {
            throw IllegalArgumentException("Belirtilen aralıkta $count benzersiz sayılar oluşturulamıyor.")
        }

        val uniqueRandomNumbers = mutableSetOf<Int>()
        while (uniqueRandomNumbers.size < count) {
            uniqueRandomNumbers.add(Random.nextInt(range))
        }
        uniqueRandomNumbers.toList()
    }
}