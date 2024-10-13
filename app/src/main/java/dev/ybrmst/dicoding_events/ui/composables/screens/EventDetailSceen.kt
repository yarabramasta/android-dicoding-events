package dev.ybrmst.dicoding_events.ui.composables.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import dev.ybrmst.dicoding_events.domain.EventDetail
import dev.ybrmst.dicoding_events.ui.composables.atoms.ShimmerBox
import dev.ybrmst.dicoding_events.ui.composables.atoms.shimmerBrush
import dev.ybrmst.dicoding_events.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EventDetailScreen(
  eventId: Int,
  navController: NavHostController,
) {
  EventDetailScreenContent(
    event = eventDetail,
    isLoading = false,
    isError = false,
    onPop = { navController.navigateUp() },
    onRetry = { /* TODO: Retry fetching event detail */ },
  )
}

private fun formatDate(dateStr: String): String {
  val inputFormatter = SimpleDateFormat(
    "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
  )
  val outputFormatter = SimpleDateFormat(
    "EEE, d MMM yyyy HH:mm a", Locale.getDefault()
  )

  return outputFormatter.format(inputFormatter.parse(dateStr)!!)
}

@Composable
private fun EventDetailScreenContent(
  event: EventDetail,
  onPop: () -> Unit,
  onRetry: () -> Unit = {},
  isLoading: Boolean = false,
  isError: Boolean = false,
) {
  val context = LocalContext.current

  Scaffold(
    bottomBar = {
      ContentBottomBar(
        context = context,
        isLoading = isLoading,
        isError = isError,
        onRetry = onRetry,
        link = event.link
      )
    }
  ) { innerPadding ->
    var showShimmer by remember { mutableStateOf(true) }

    Box {
      ContentTopBar(onPop = onPop)
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
      ) {
        if (!(isLoading || isError)) {
          item {
            Box(modifier = Modifier.height(280.dp)) {
              AsyncImage(
                model = event.mediaCover,
                contentDescription = event.name,
                alignment = Alignment.TopCenter,
                contentScale = ContentScale.FillBounds,
                onState = {
                  showShimmer = when (it) {
                    AsyncImagePainter.State.Empty -> true
                    is AsyncImagePainter.State.Error -> false
                    is AsyncImagePainter.State.Loading -> true
                    is AsyncImagePainter.State.Success -> false
                  }
                },
                modifier = Modifier
                  .fillMaxSize()
                  .background(
                    shimmerBrush(
                      show = showShimmer,
                      targetValue = 1300f
                    )
                  )
              )
            }
          }
        }
        when {
          isLoading -> buildLoadingFallback()

          isError -> {
            item {
              Text(
                text = "Failed to load event detail. Please try again.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
              )
            }
          }

          else -> buildContent(event)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentTopBar(onPop: () -> Unit) {
  TopAppBar(title = {}, navigationIcon = {
    IconButton(onClick = onPop) {
      Icon(
        imageVector = Icons.Filled.ArrowBackIosNew,
        contentDescription = "Back",
        tint = Color.Black
      )
    }
  },
    colors = TopAppBarDefaults.largeTopAppBarColors().copy(
      containerColor = Color.Transparent,
      scrolledContainerColor = Color.Transparent
    ),
    modifier = Modifier
      .fillMaxWidth()
      .zIndex(1f)
  )
}

@Composable
private fun ContentBottomBar(
  context: Context = LocalContext.current,
  isLoading: Boolean = false,
  isError: Boolean = false,
  onRetry: () -> Unit = {},
  link: String,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface)
      .padding(16.dp)
  ) {
    if (!isLoading) {
      if (isError) {
        Button(
          colors = ButtonDefaults.filledTonalButtonColors().copy(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
          ),
          onClick = { onRetry() },
          modifier = Modifier.fillMaxWidth(),
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.Refresh,
              contentDescription = null,
              modifier = Modifier.padding(end = 8.dp),
            )
            Text("Retry")
          }
        }
      } else {
        FilledTonalButton(
          onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(intent)
          }, modifier = Modifier.fillMaxWidth()
        ) { Text("Register") }
      }
    }
  }
}

private fun LazyListScope.buildContent(event: EventDetail) {
  item {
    Text(
      event.cityName,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.secondary,
      modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      event.name,
      style = MaterialTheme.typography.titleLarge,
      color = MaterialTheme.colorScheme.primary,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      "By ${event.ownerName}",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.outline,
      modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      event.summary,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.outline,
      modifier = Modifier.padding(horizontal = 24.dp)
    )
  }
  item {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp),
    ) {
      Icon(
        imageVector = Icons.Filled.Category,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        event.category,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.tertiary,
      )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
    ) {
      Icon(
        imageVector = Icons.Filled.Event,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        "${formatDate(event.beginTime)} - ${formatDate(event.endTime)}",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.tertiary,
      )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
    ) {
      Icon(
        imageVector = Icons.Filled.PeopleAlt,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        "Quota ${event.quota} | Registrants ${event.registrants}",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.tertiary,
      )
    }
  }
  item {
    Text(
      event.description,
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(horizontal = 24.dp)
    )
  }
}

private fun LazyListScope.buildLoadingFallback() {
  item {
    ShimmerBox(
      modifier = Modifier
        .width(64.dp)
        .height(16.dp)
        .padding(horizontal = 24.dp)
    )
    ShimmerBox(
      modifier = Modifier
        .padding(vertical = 8.dp)
        .fillMaxWidth()
        .height(32.dp)
        .padding(horizontal = 24.dp)
    )
    ShimmerBox(
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(horizontal = 24.dp)
    )
  }
  item {
    ShimmerBox(
      modifier = Modifier
        .width(240.dp)
        .height(64.dp)
        .padding(horizontal = 24.dp)
    )
  }
  item {
    ShimmerBox(
      modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .padding(horizontal = 24.dp)
    )
  }
}

private val eventDetail = EventDetail(
  id = 8948,
  name = "[Offline] IDCamp Connect  Roadshow - Solo",
  summary = "IDCamp Connect Roadshow 2024 akan dilaksanakan pada hari Jumat, 18 Oktober 2024 pukul 13.00 - 17.00 WIB",
  description = "<p><img src=\"https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/event/dos-aee8770af1386a16c07b14e36ae328d520241009121846.jpg\" class=\"fr-fic fr-dib\" alt=\"dos-aee8770af1386a16c07b14e36ae328d520241009121846.jpg\" /></p><p dir=\"ltr\">Sejak tahun 2019, IDCamp telah memberikan lebih dari 270.000 beasiswa coding dan berhasil melahirkan 91.000+ lulusan terampil. Di tahun 2024, IDCamp kembali hadir untuk melanjutkan misinya dengan  menyediakan ratusan ribu akses belajar coding secara gratis untuk para calon developer teknologi Indonesia.</p><p dir=\"ltr\">IDCamp 2024 menawarkan beasiswa pelatihan coding online di <strong>8 pilihan alur belajar utama</strong> mulai dari level dasar hingga mahir yang mencakup Android Developer, Front-End Web Developer, Machine Learning Engineer, Multi-Platform App Developer, Back-End Developer, React Developer, dan Data Scientist. Tak hanya itu, di tahun 2024 ini IDCamp juga memberikan <strong>3 alur belajar/kelas tambahan</strong> di bidang <strong>Artificial Intelligence</strong>, <strong>Cyber Security</strong>, dan <strong>Automation</strong>.</p><p dir=\"ltr\">Dalam rangka menjangkau lebih banyak pendaftar beasiswa IDCamp 2024, Indosat Ooredoo Hutchison bersama Dicoding akan mengadakan event offline roadshow yang tergabung dalam “<strong>IDCamp Connect</strong>”. <strong>Roadshow </strong>ini akan dimulai di kota Solo.</p><p dir=\"ltr\"><strong><span style=\"font-size:24px;\">Peserta yang bisa mendaftar</span></strong></p><ul><li dir=\"ltr\">Berdomisili di kota Solo dan sekitarnya</li><li>Diprioritaskan yang memiliki track record baik dalam program Dicoding atau IDCamp</li><li>Dalam keadaan sehat dan fit</li></ul><hr /><p><strong><span style=\"color:rgb(226,80,65);\">Catatan: Karena keterbatasan kuota, maka tim Dicoding dan IDCamp akan menyeleksi peserta yang beruntung untuk terpilih menghadiri event ini. Silakan ikuti cara mendaftar event ini di bagian bawah.</span></strong></p><hr /><p><strong><span style=\"font-size:24px;\">Benefit terpilih menjadi peserta IDCamp Connect Roadshow - Solo</span></strong></p><ul><li>Insight dari Experts</li><li>Networking sesama Developer dan Expert</li><li>Sertifikat kehadiran digital</li><li>Konsumsi</li><li>Doorprize menarik</li></ul><p dir=\"ltr\"><span style=\"font-size:24px;\"><strong>Cara Mendaftar</strong></span></p><ol><li dir=\"ltr\"><p dir=\"ltr\">Pendaftar event harus <strong>melengkapi data profil Dicoding</strong>nya (Cara update dapat dilihat <a href=\"https://help.dicoding.com/akun/cara-update-data-profil-akun-dicoding/?_gl=1*wxmotl*_gcl_au*NjQ4NzcyMjEzLjE3MjM0MjMzOTQ.\" target=\"_blank\" rel=\"noreferrer noopener\"><strong>di sini</strong></a>) sebelum event berlangsung.</p></li><li dir=\"ltr\"><p dir=\"ltr\">Klik “<strong>Daftar ke waiting list</strong>” di halaman event <strong>IDCamp Connect Roadshow #1 - Solo</strong> ini.</p></li><li dir=\"ltr\"><p dir=\"ltr\">Tim Dicoding akan <strong>menyeleksi </strong>pendaftar secara berkala (<em>maksimal hingga H-3 acara</em>). Peserta yang terpilih akan menerima konfirmasi melalui Whatsapp. Oleh karena itu, mohon pastikan point nomor 1 sudah dilengkapi terutama nomor Whatsapp yang dapat dihubungi.</p></li><li dir=\"ltr\"><p dir=\"ltr\">Pendaftar yang telah terpilih harus melakukan konfirmasi kehadiran maksimal H+1 setelah menerima Whatsapp. Apabila tidak melakukan konfirmasi dalam jangka waktu yang ditentukan, kuota akan hangus dan diberikan kepada pendaftar lainnya.</p></li></ol><p dir=\"ltr\"><span style=\"font-size:24px;\"><strong>Rundown Acara</strong></span></p><p dir=\"ltr\">13.00 - 14.00: Registration and Lunch<br />14.00 - 14.20: Opening<br />14.20 - 14.25: Foto Bersama<br />14.25 - 14.40: Ashar dan Coffee Break<br />14.40 - 14.45: Intro to Seminar<br />14.45 - 15.35: Seminar - <strong><em>AI in Your Hands: Essential Basics and Practical Guide to Get You Started</em></strong><br />15.35 - 16.25: Seminar - <strong><em>Empowering Mobile Apps with Generative AI: A Smart Split Bill Case Study</em></strong><br />16.25 - 16.55: QnA<br />16.55 - 17.00: Doorprize and Closing</p><p dir=\"ltr\"><span style=\"font-size:24px;\"><strong>FAQ</strong></span></p><ol><li dir=\"ltr\"><p dir=\"ltr\">Saya sudah mendaftar, bagaimana caranya agar saya mengetahui bahwa saya terpilih/tidak terpilih untuk hadir di event ini?<br /><em>Jawab: Apabila setelah mendaftar kamu terpilih untuk hadir ke event ini, kamu akan dikonfirmasi melalui WhatsApp dan akan diinformasikan terkait detail event ini.</em></p></li><li dir=\"ltr\"><p dir=\"ltr\">Apakah saya akan mendapatkan sertifikat apabila telah hadir di event ini?<br /><em>Jawab: Ya, kamu akan mendapatkan sertifikat ini melalui halaman event <a href=\"http://dicoding.id/idcamp-connect-solo\" target=\"_blank\" rel=\"noreferrer noopener\">dicoding.id/idcamp-connect-solo</a> maksimal h+3 selama hari kerja setelah event berlangsung.</em></p></li><li dir=\"ltr\"><p dir=\"ltr\">Apakah saya perlu membawa laptop?<br /><em>Jawab: Tidak perlu membawa laptop.</em></p></li><li><p>Apakah saya boleh membawa teman saya yang tidak terdaftar di event ini?<br /><em>Jawab: Event ini bersifat private jadi hanya yang terpilih sebagai peserta saja yang dapat hadir. 1 undangan hanya berlaku untuk 1 orang.</em></p></li></ol>",
  imageLogo = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/event/dos-offline_idcamp_connect_roadshow_solo_logo_091024131113.png",
  mediaCover = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/event/dos-offline_idcamp_connect_roadshow_solo_mc_091024131113.jpg",
  category = "Seminar",
  ownerName = "Dicoding Operational",
  cityName = "Kota Surakarta",
  quota = 10,
  registrants = 10,
  beginTime = "2024-10-18 13:00:00",
  endTime = "2024-10-18 17:00:00",
  link = "https://www.dicoding.com/events/8948"
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DefaultStatePreview() {

  AppTheme {
    EventDetailScreenContent(event = eventDetail, onPop = {})
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoadingStatePreview() {

  AppTheme {
    EventDetailScreenContent(
      event = eventDetail,
      isLoading = true,
      onPop = {},
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ErrorStatePreview() {

  AppTheme {
    EventDetailScreenContent(
      event = eventDetail,
      isError = true,
      onPop = {},
    )
  }
}