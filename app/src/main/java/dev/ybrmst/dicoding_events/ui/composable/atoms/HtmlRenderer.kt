package dev.ybrmst.dicoding_events.ui.composable.atoms

import android.content.Intent
import android.net.Uri
import android.text.style.URLSpan
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.core.text.HtmlCompat

@Suppress("DEPRECATION")
@Composable
fun HtmlRenderer(
  modifier: Modifier = Modifier,
  html: String,
  linkColor: Color = MaterialTheme.colorScheme.primary,
  fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
) {
  val context = LocalContext.current

  val sanitizedHtml = remember(html) {
    html
      .replace("<img[^>]*>".toRegex(), "")
      .trimIndent()
  }

  val annotatedText = remember(sanitizedHtml) {
    val spanned =
      HtmlCompat.fromHtml(
        sanitizedHtml,
        HtmlCompat.FROM_HTML_MODE_LEGACY
      )
    val text = spanned.toString()

    buildAnnotatedString {
      append(text)

      val urlSpans = spanned.getSpans(0, spanned.length, URLSpan::class.java)
      urlSpans.forEach { urlSpan ->
        val start = spanned.getSpanStart(urlSpan)
        val end = spanned.getSpanEnd(urlSpan)
        val url = urlSpan.url

        addStyle(
          start = start,
          end = end,
          style = SpanStyle(
            color = linkColor,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline
          ),
        )
        addStringAnnotation(
          tag = "URL",
          annotation = url,
          start = start,
          end = end
        )
      }
    }
  }

  ClickableText(
    modifier = modifier,
    text = annotatedText,
    style = MaterialTheme.typography.bodyLarge.copy(
      fontSize = fontSize,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    ),
    onClick = { offset ->
      annotatedText.getStringAnnotations(
        tag = "URL",
        start = offset,
        end = offset
      )
        .firstOrNull()?.let { annotation ->
          val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
          context.startActivity(intent)
        }
    },
  )
}

@Preview(showBackground = true)
@Composable
private fun HtmlRendererPreview() {
  HtmlRenderer(
    html = "<p><img src=\"https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/event/dos-aee8770af1386a16c07b14e36ae328d520241009121846.jpg\" class=\"fr-fic fr-dib\" alt=\"dos-aee8770af1386a16c07b14e36ae328d520241009121846.jpg\" /></p><p dir=\"ltr\">Sejak tahun 2019, IDCamp telah memberikan lebih dari 270.000 beasiswa coding dan berhasil melahirkan 91.000+ lulusan terampil. Di tahun 2024, IDCamp kembali hadir untuk melanjutkan misinya dengan  menyediakan ratusan ribu akses belajar coding secara gratis untuk para calon developer teknologi Indonesia.</p><p dir=\"ltr\">IDCamp 2024 menawarkan beasiswa pelatihan coding online di <strong>8 pilihan alur belajar utama</strong> mulai dari level dasar hingga mahir yang mencakup Android Developer, Front-End Web Developer, Machine Learning Engineer, Multi-Platform App Developer, Back-End Developer, React Developer, dan Data Scientist. Tak hanya itu, di tahun 2024 ini IDCamp juga memberikan <strong>3 alur belajar/kelas tambahan</strong> di bidang <strong>Artificial Intelligence</strong>, <strong>Cyber Security</strong>, dan <strong>Automation</strong>.</p><p dir=\"ltr\">Dalam rangka menjangkau lebih banyak pendaftar beasiswa IDCamp 2024, Indosat Ooredoo Hutchison bersama Dicoding akan mengadakan event offline roadshow yang tergabung dalam “<strong>IDCamp Connect</strong>”. <strong>Roadshow </strong>ini akan dimulai di kota Solo.</p><p dir=\"ltr\"><strong><span style=\"font-size:24px;\">Peserta yang bisa mendaftar</span></strong></p><ul><li dir=\"ltr\">Berdomisili di kota Solo dan sekitarnya</li><li>Diprioritaskan yang memiliki track record baik dalam program Dicoding atau IDCamp</li><li>Dalam keadaan sehat dan fit</li></ul><hr /><p><strong><span style=\"color:rgb(226,80,65);\">Catatan: Karena keterbatasan kuota, maka tim Dicoding dan IDCamp akan menyeleksi peserta yang beruntung untuk terpilih menghadiri event ini. Silakan ikuti cara mendaftar event ini di bagian bawah.</span></strong></p><hr /><p><strong><span style=\"font-size:24px;\">Benefit terpilih menjadi peserta IDCamp Connect Roadshow - Solo</span></strong></p><ul><li>Insight dari Experts</li><li>Networking sesama Developer dan Expert</li><li>Sertifikat kehadiran digital</li><li>Konsumsi</li><li>Doorprize menarik</li></ul><p dir=\"ltr\"><span style=\"font-size:24px;\"><strong>Cara Mendaftar</strong></span></p><ol><li dir=\"ltr\"><p dir=\"ltr\">Pendaftar event harus <strong>melengkapi data profil Dicoding</strong>nya (Cara update dapat dilihat <a href=\"https://help.dicoding.com/akun/cara-update-data-profil-akun-dicoding/?_gl=1*wxmotl*_gcl_au*NjQ4NzcyMjEzLjE3MjM0MjMzOTQ.\" target=\"_blank\" rel=\"noreferrer noopener\"><strong>di sini</strong></a>) sebelum event berlangsung.</p></li><li dir=\"ltr\"><p dir=\"ltr\">Klik “<strong>Daftar ke waiting list</strong>” di halaman event <strong>IDCamp Connect Roadshow #1 - Solo</strong> ini.</p></li><li dir=\"ltr\"><p dir=\"ltr\">Tim Dicoding akan <strong>menyeleksi </strong>pendaftar secara berkala (<em>maksimal hingga H-3 acara</em>). Peserta yang terpilih akan menerima konfirmasi melalui Whatsapp. Oleh karena itu, mohon pastikan point nomor 1 sudah dilengkapi terutama nomor Whatsapp yang dapat dihubungi.</p></li><li dir=\"ltr\"><p dir=\"ltr\">Pendaftar yang telah terpilih harus melakukan konfirmasi kehadiran maksimal H+1 setelah menerima Whatsapp. Apabila tidak melakukan konfirmasi dalam jangka waktu yang ditentukan, kuota akan hangus dan diberikan kepada pendaftar lainnya.</p></li></ol><p dir=\"ltr\"><span style=\"font-size:24px;\"><strong>Rundown Acara</strong></span></p><p dir=\"ltr\">13.00 - 14.00: Registration and Lunch<br />14.00 - 14.20: Opening<br />14.20 - 14.25: Foto Bersama<br />14.25 - 14.40: Ashar dan Coffee Break<br />14.40 - 14.45: Intro to Seminar<br />14.45 - 15.35: Seminar - <strong><em>AI in Your Hands: Essential Basics and Practical Guide to Get You Started</em></strong><br />15.35 - 16.25: Seminar - <strong><em>Empowering Mobile Apps with Generative AI: A Smart Split Bill Case Study</em></strong><br />16.25 - 16.55: QnA<br />16.55 - 17.00: Doorprize and Closing</p><p dir=\"ltr\"><span style=\"font-size:24px;\"><strong>FAQ</strong></span></p><ol><li dir=\"ltr\"><p dir=\"ltr\">Saya sudah mendaftar, bagaimana caranya agar saya mengetahui bahwa saya terpilih/tidak terpilih untuk hadir di event ini?<br /><em>Jawab: Apabila setelah mendaftar kamu terpilih untuk hadir ke event ini, kamu akan dikonfirmasi melalui WhatsApp dan akan diinformasikan terkait detail event ini.</em></p></li><li dir=\"ltr\"><p dir=\"ltr\">Apakah saya akan mendapatkan sertifikat apabila telah hadir di event ini?<br /><em>Jawab: Ya, kamu akan mendapatkan sertifikat ini melalui halaman event <a href=\"http://dicoding.id/idcamp-connect-solo\" target=\"_blank\" rel=\"noreferrer noopener\">dicoding.id/idcamp-connect-solo</a> maksimal h+3 selama hari kerja setelah event berlangsung.</em></p></li><li dir=\"ltr\"><p dir=\"ltr\">Apakah saya perlu membawa laptop?<br /><em>Jawab: Tidak perlu membawa laptop.</em></p></li><li><p>Apakah saya boleh membawa teman saya yang tidak terdaftar di event ini?<br /><em>Jawab: Event ini bersifat private jadi hanya yang terpilih sebagai peserta saja yang dapat hadir. 1 undangan hanya berlaku untuk 1 orang.</em></p></li></ol>"
  )
}