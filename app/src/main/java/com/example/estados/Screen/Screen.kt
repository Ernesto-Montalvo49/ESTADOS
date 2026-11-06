package com.example.estados.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculadoraScreen(modifier: Modifier) {

    var x by rememberSaveable { mutableStateOf("") }
    var y by rememberSaveable { mutableStateOf("") }
    var ans by rememberSaveable { mutableStateOf(0) }

    LazyColumn(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.padding(10.dp))

                TextField(
                    value = x,
                    onValueChange = { n -> x = n.filter { it.isDigit() } },
                    label = { Text("Numero 1") },
                    placeholder = { Text("Escribe el valor 1") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(10.dp))

                TextField(
                    value = y,
                    onValueChange = { nuevo -> y = nuevo.filter { it.isDigit() } },
                    label = { Text("Numero 2") },
                    placeholder = { Text("Escribe el valor 2") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(15.dp))
                Spacer(modifier = Modifier.padding(10.dp))
                Text("Resultado: " + ans, fontSize = 20.sp)
                Spacer(modifier = Modifier.padding(15.dp))
            }
        }
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        if (x.isNotEmpty() && y.isNotEmpty()) {
                            ans = x.toInt() + y.toInt()
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Text("Suma")
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Button(onClick = {
                        if (x.isNotEmpty() && y.isNotEmpty()) {
                            ans = x.toInt() - y.toInt()
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Text("Resta")
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        if (x.isNotEmpty() && y.isNotEmpty()) {
                            ans = x.toInt() * y.toInt()
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Text("Multiplicacion")
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Button(onClick = {
                        if (x.isNotEmpty() && y.isNotEmpty() && y.toInt() != 0) {
                            ans = x.toInt() / y.toInt()
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Text("Division")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Calculadorapreview() {
    CalculadoraScreen(modifier = Modifier.fillMaxSize())
}