@file:OptIn(ExperimentalForeignApi::class, ExperimentalMaterial3Api::class)

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import kotlinx.cinterop.ExperimentalForeignApi

fun main() = application {
    Window(title = "Compose MD3 mingwX64 Demo") {
        var isDark by remember { mutableStateOf(false) }
        MaterialTheme(colorScheme = if (isDark) darkColorScheme() else lightColorScheme()) {
            App(isDark = isDark, onToggleDark = { isDark = it })
        }
    }
}

@Composable
fun App(isDark: Boolean, onToggleDark: (Boolean) -> Unit) {
    var currentTab by remember { mutableStateOf(0) }
    val tabs = listOf("Widgets", "Inputs", "Info")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Compose mingwX64") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                actions = {
                    IconButton(onClick = { onToggleDark(!isDark) }) {
                        Icon(
                            imageVector = if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = "Toggle theme",
                        )
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = index == currentTab,
                        onClick = { currentTab = index },
                        label = { Text(title) },
                        icon = {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Filled.Widgets
                                    1 -> Icons.Filled.EditNote
                                    else -> Icons.Filled.Info
                                },
                                contentDescription = title,
                            )
                        },
                    )
                }
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { Spacer(Modifier.height(0.dp)) }
            item {
                when (currentTab) {
                    0 -> WidgetsPage()
                    1 -> InputsPage()
                    2 -> InfoPage()
                }
            }
            item { Spacer(Modifier.height(0.dp)) }
        }
    }
}

// ==================== Widgets Page ====================

@Composable
fun WidgetsPage() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        CounterSection()
        ChipsSection()
        CheckboxSection()
    }
}

@Composable
fun CounterSection() {
    var count by remember { mutableStateOf(0) }

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Counter", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))
            Text(
                text = "$count",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { count-- }) { Text("- 1") }
                FilledTonalButton(onClick = { count = 0 }) { Text("Reset") }
                Button(onClick = { count++ }) { Text("+ 1") }
            }
        }
    }
}

@Composable
fun ChipsSection() {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Filter Chips", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            val labels = listOf("Kotlin", "Compose", "MinGW", "Native", "ANGLE", "Material 3")
            var selected by remember { mutableStateOf(setOf(0, 1)) }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                labels.forEachIndexed { i, label ->
                    FilterChip(
                        selected = i in selected,
                        onClick = {
                            selected = if (i in selected) selected - i else selected + i
                        },
                        label = { Text(label) },
                    )
                }
            }

            if (selected.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Selected: ${selected.sorted().map { labels[it] }.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun CheckboxSection() {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Checkboxes", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))

            val items = listOf("GPU Rendering", "DPI Awareness", "Text Input", "IME Support")
            var checked by remember { mutableStateOf(setOf(0, 1, 2, 3)) }

            items.forEachIndexed { i, label ->
                ListItem(
                    headlineContent = { Text(label) },
                    trailingContent = {
                        Checkbox(
                            checked = i in checked,
                            onCheckedChange = {
                                checked = if (i in checked) checked - i else checked + i
                            },
                        )
                    },
                )
                if (i < items.lastIndex) HorizontalDivider()
            }
        }
    }
}

// ==================== Inputs Page ====================

@Composable
fun InputsPage() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        TextFieldSection()
        SliderSection()
        SwitchSection()
    }
}

@Composable
fun TextFieldSection() {
    var text by remember { mutableStateOf("") }

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Text Input", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Type something") },
                supportingText = {
                    if (text.isNotEmpty()) Text("${text.length} characters")
                },
                modifier = Modifier.fillMaxWidth(),
            )
            if (text.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = { text = "" }) { Text("Clear") }
                }
            }
        }
    }
}

@Composable
fun SliderSection() {
    var value by remember { mutableStateOf(0.5f) }

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Slider", style = MaterialTheme.typography.titleMedium)
                Text(
                    "${(value * 100).toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(Modifier.height(8.dp))
            Slider(value = value, onValueChange = { value = it })
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { value },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun SwitchSection() {
    var wifi by remember { mutableStateOf(true) }
    var bluetooth by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }

    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Settings", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))

            val switches = listOf(
                Triple("Wi-Fi", wifi) { v: Boolean -> wifi = v },
                Triple("Bluetooth", bluetooth) { v: Boolean -> bluetooth = v },
                Triple("Notifications", notifications) { v: Boolean -> notifications = v },
            )

            switches.forEachIndexed { i, (label, state, onChange) ->
                ListItem(
                    headlineContent = { Text(label) },
                    supportingContent = { Text(if (state) "Enabled" else "Disabled") },
                    trailingContent = { Switch(checked = state, onCheckedChange = onChange) },
                )
                if (i < switches.lastIndex) HorizontalDivider()
            }
        }
    }
}

// ==================== Info Page ====================

@Composable
fun InfoPage() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        ) {
            Column(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("K", fontSize = 28.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Compose for mingwX64",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    "Native Windows without JVM",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
            }
        }

        InfoCard("Tech Stack", listOf(
            "Kotlin/Native" to "2.3.20",
            "Compose Multiplatform" to "1.11.0-alpha04",
            "Skiko" to "0.144.5",
            "Material 3" to "1.5.0-alpha13",
            "Renderer" to "ANGLE",
        ))

        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Button Styles", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(onClick = {}) { Text("Filled") }
                    ElevatedButton(onClick = {}) { Text("Elevated") }
                    FilledTonalButton(onClick = {}) { Text("Tonal") }
                    OutlinedButton(onClick = {}) { Text("Outlined") }
                    TextButton(onClick = {}) { Text("Text") }
                }
            }
        }
    }
}

@Composable
fun InfoCard(title: String, items: List<Pair<String, String>>) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            items.forEachIndexed { i, (key, value) ->
                ListItem(
                    headlineContent = { Text(key) },
                    supportingContent = { Text(value) },
                )
                if (i < items.lastIndex) HorizontalDivider()
            }
        }
    }
}
