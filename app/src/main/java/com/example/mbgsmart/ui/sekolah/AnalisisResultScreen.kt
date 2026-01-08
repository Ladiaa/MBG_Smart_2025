package com.example.mbgsmart.ui.sekolah

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mbgsmart.data.model.Menu
import com.example.mbgsmart.data.model.asText
import com.example.mbgsmart.ui.components.BaseScreen
import com.example.mbgsmart.ui.components.PageTitleCard
import com.example.mbgsmart.ui.theme.*
import com.example.mbgsmart.ui.viewmodel.MenuViewModel

@Composable
fun AnalisisResultScreen(
    menu: Menu,
    menuViewModel: MenuViewModel,
    onFinish: () -> Unit
) {
    var isUploading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val score = remember(menu) {
        listOf(
            menu.karbo,
            menu.protein,
            menu.sayur,
            menu.buah,
            menu.minuman
        ).count { it.asText().isNotBlank() } * 20
    }

    BaseScreen(
        title = "Hasil Analisis Menu",
        subtitle = "Analisis kelengkapan gizi menu MBG"
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ScoreCard(
                score = score,
                color = if (score >= 80) BrandGreen else BrandRed
            )

            AnalysisDetailCard(
                results = listOf(
                    NutrientAnalysis("Karbohidrat", menu.karbo.asText().isNotBlank()),
                    NutrientAnalysis("Protein", menu.protein.asText().isNotBlank()),
                    NutrientAnalysis("Sayur", menu.sayur.asText().isNotBlank()),
                    NutrientAnalysis("Buah", menu.buah.asText().isNotBlank()),
                    NutrientAnalysis("Minuman", menu.minuman.asText().isNotBlank())
                )
            )

            FeedbackCard(score)

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !isUploading,
                onClick = {
                    isUploading = true
                    menuViewModel.createMenu(menu) { success ->
                        isUploading = false
                        if (success) onFinish()
                        else errorMessage = "Gagal menyimpan menu"
                    }
                }
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Posting Menu")
                }
            }
        }
    }
}



/* ================= MODEL ================= */

data class NutrientAnalysis(
    val title: String,
    val available: Boolean
)


/* ================= COMPONENT ================= */

@Composable
fun ScoreCard(score: Int, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Skor Nutrisi", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = score.toString(),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                if (score >= 80) "Sangat Baik" else "Perlu Perbaikan",
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
fun AnalysisDetailCard(results: List<NutrientAnalysis>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "Detail Komposisi",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = SecondaryBlue
            )
            Spacer(Modifier.height(12.dp))
            results.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector =
                            if (it.available) Icons.Filled.CheckCircle
                            else Icons.Filled.Cancel,
                        contentDescription = null,
                        tint =
                            if (it.available) BrandGreen
                            else BrandRed
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(it.title, modifier = Modifier.weight(1f))
                    Text(if (it.available) "Ada" else "Tidak Ada")
                }
            }
        }
    }
}

@Composable
fun FeedbackCard(score: Int) {
    val positive = score >= 80
    val accent = if (positive) BrandGreen else BrandRed

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .background(accent.copy(alpha = 0.1f))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (positive) Icons.Filled.ThumbUp else Icons.Filled.Warning,
                contentDescription = null,
                tint = accent
            )
            Spacer(Modifier.width(10.dp))
            Text(
                if (positive)
                    "Menu sudah seimbang, pertahankan."
                else
                    "Menu belum seimbang, perlu perbaikan."
            )
        }
    }
}
