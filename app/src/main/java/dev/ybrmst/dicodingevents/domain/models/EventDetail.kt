package dev.ybrmst.dicodingevents.domain.models

import dev.ybrmst.dicodingevents.lib.DateTimeParser
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class EventDetail(
  val id: Int,
  val name: String,
  val summary: String,
  val description: String,
  val imageLogo: String,
  val mediaCover: String,
  val category: String,
  val ownerName: String,
  val cityName: String,
  val quota: Int,
  val registrants: Int,
  val link: String,
  val isFavorite: Boolean = false,

  @Serializable(with = DateTimeParser::class)
  @SerialName("beginTime")
  val beginDate: LocalDateTime,

  @Serializable(with = DateTimeParser::class)
  @SerialName("endTime")
  val endDate: LocalDateTime,
) {
  companion object {
    fun fake() = EventDetail(
      id = Random.nextInt(),
      name = "[Offline] IDCamp Connect Roadshow - Solo",
      summary = "IDCamp Connect Roadshow 2024 akan dilaksanakan pada hari Jumat, 18 Oktober 2024 pukul 13.00 - 17.00 WIB",
      imageLogo = "https://placehold.co/400.png",
      mediaCover = "https://placehold.co/1080x720.png",
      category = "Seminar",
      ownerName = "Dicoding Operational",
      cityName = "Kota Surakarta",
      quota = 10,
      registrants = 10,
      link = "https://www.dicoding.com/events/8948",
      beginDate = LocalDateTime.parse("2024-10-18T13:00:00"),
      endDate = LocalDateTime.parse("2024-10-18T17:00:00"),
      description = """
            <p><img src="https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/event/dos-aee8770af1386a16c07b14e36ae328d520241009121846.jpg" class="fr-fic fr-dib" alt="dos-aee8770af1386a16c07b14e36ae328d520241009121846.jpg" /></p>
            <p dir="ltr">Sejak tahun 2019, IDCamp telah memberikan lebih dari 270.000 beasiswa coding dan berhasil melahirkan 91.000+ lulusan terampil. Di tahun 2024, IDCamp kembali hadir untuk melanjutkan misinya dengan  menyediakan ratusan ribu akses belajar coding secara gratis untuk para calon developer teknologi Indonesia.</p>
            <p dir="ltr">IDCamp 2024 menawarkan beasiswa pelatihan coding online di <strong>8 pilihan alur belajar utama</strong> mulai dari level dasar hingga mahir yang mencakup Android Developer, Front-End Web Developer, Machine Learning Engineer, Multi-Platform App Developer, Back-End Developer, React Developer, dan Data Scientist. Tak hanya itu, di tahun 2024 ini IDCamp juga memberikan <strong>3 alur belajar/kelas tambahan</strong> di bidang <strong>Artificial Intelligence</strong>, <strong>Cyber Security</strong>, dan <strong>Automation</strong>.</p>
            <p dir="ltr">Dalam rangka menjangkau lebih banyak pendaftar beasiswa IDCamp 2024, Indosat Ooredoo Hutchison bersama Dicoding akan mengadakan event offline roadshow yang tergabung dalam “<strong>IDCamp Connect</strong>”. <strong>Roadshow</strong> ini akan dimulai di kota Solo.</p>
            <p dir="ltr"><strong><span style="font-size:24px;">Peserta yang bisa mendaftar</span></strong></p>
            <ul>
                <li dir="ltr">Berdomisili di kota Solo dan sekitarnya</li>
                <li>Diprioritaskan yang memiliki track record baik dalam program Dicoding atau IDCamp</li>
                <li>Dalam keadaan sehat dan fit</li>
            </ul>
            <hr />
            <p><strong><span style="color:rgb(226,80,65);">Catatan: Karena keterbatasan kuota, maka tim Dicoding dan IDCamp akan menyeleksi peserta yang beruntung untuk terpilih menghadiri event ini. Silakan ikuti cara mendaftar event ini di bagian bawah.</span></strong></p>
            <hr />
            <p><strong><span style="font-size:24px;">Benefit terpilih menjadi peserta IDCamp Connect Roadshow - Solo</span></strong></p>
            <ul>
                <li>Insight dari Experts</li>
                <li>Networking sesama Developer dan Expert</li>
                <li>Sertifikat kehadiran digital</li>
                <li>Konsumsi</li>
                <li>Doorprize menarik</li>
            </ul>
            <p dir="ltr"><span style="font-size:24px;"><strong>Cara Mendaftar</strong></span></p>
            <ol>
                <li dir="ltr">
                    <p dir="ltr">Pendaftar event harus <strong>melengkapi data profil Dicoding</strong>nya (Cara update dapat dilihat <a href="https://help.dicoding.com/akun/cara-update-data-profil-akun-dicoding/?_gl=1*wxmotl*_gcl_au*NjQ4NzcyMjEzLjE3MjM0MjMzOTQ." target="_blank" rel="noreferrer noopener"><strong>di sini</strong></a>) sebelum event berlangsung.</p>
                </li>
                <li dir="ltr">
                    <p dir="ltr">Klik “<strong>Daftar ke waiting list</strong>” di halaman event <strong>IDCamp Connect Roadshow #1 - Solo</strong> ini.</p>
                </li>
                <li dir="ltr">
                    <p dir="ltr">Tim Dicoding akan <strong>menyeleksi</strong> pendaftar secara berkala (<em>maksimal hingga H-3 acara</em>). Peserta yang terpilih akan menerima konfirmasi melalui Whatsapp. Oleh karena itu, mohon pastikan point nomor 1 sudah dilengkapi terutama nomor Whatsapp yang dapat dihubungi.</p>
                </li>
                <li dir="ltr">
                    <p dir="ltr">Pendaftar yang telah terpilih harus melakukan konfirmasi kehadiran maksimal H+1 setelah menerima Whatsapp. Apabila tidak melakukan konfirmasi dalam jangka waktu yang ditentukan, kuota akan hangus dan diberikan kepada pendaftar lainnya.</p>
                </li>
            </ol>
            <p dir="ltr"><span style="font-size:24px;"><strong>Rundown Acara</strong></span></p>
            <p dir="ltr">13.00 - 14.00: Registration and Lunch<br />14.00 - 14.20: Opening<br />14.20 - 14.25: Foto Bersama<br />14.25 - 14.40: Ashar dan Coffee Break<br />14.40 - 14.45: Intro to Seminar<br />14.45 - 15.35: Seminar - <strong><em>AI in Your Hands: Essential Basics and Practical Guide to Get You Started</em></strong><br />15.35 - 16.25: Seminar - <strong><em>Empowering Mobile Apps with Generative AI: A Smart Split Bill Case Study</em></strong><br />16.25 - 16.55: QnA<br />16.55 - 17.00: Doorprize and Closing</p>
            <p dir="ltr"><span style="font-size:24px;"><strong>FAQ</strong></span></p>
            <ol>
                <li dir="ltr">
                    <p dir="ltr">Saya sudah mendaftar, bagaimana caranya agar saya mengetahui bahwa saya terpilih/tidak terpilih untuk hadir di event ini?<br /><em>Jawab: Apabila setelah mendaftar kamu terpilih untuk hadir ke event ini, kamu akan dikonfirmasi melalui WhatsApp dan akan diinformasikan terkait detail event ini.</em></p>
                </li>
                <li dir="ltr">
                    <p dir="ltr">Apakah saya akan mendapatkan sertifikat apabila telah hadir di event ini?<br /><em>Jawab: Ya, kamu akan mendapatkan sertifikat ini melalui halaman event <a href="http://dicoding.id/idcamp-connect-solo" target="_blank" rel="noreferrer noopener">dicoding.id/idcamp-connect-solo</a> maksimal h+3 selama hari kerja setelah event berlangsung.</em></p>
                </li>
                <li dir="ltr">
                    <p dir="ltr">Apakah saya perlu membawa laptop?<br /><em>Jawab: Tidak perlu membawa laptop.</em></p>
                </li>
                <li>
                    <p>Apakah saya boleh membawa teman saya yang tidak terdaftar di event ini?<br /><em>Jawab: Event ini bersifat private jadi hanya yang terpilih sebagai peserta saja yang dapat hadir. 1 undangan hanya berlaku untuk 1 orang.</em></p>
                </li>
            </ol>
        """.trimIndent(),
    )
  }
}

internal fun extractTime(
  date: LocalDateTime,
) = LocalDateTime.Format {
  time(
    LocalTime.Format {
      hour(); char(':'); minute()
    }
  )
}.format(date)

internal fun formatDatePretty(
  date: LocalDateTime,
  includeWeekDay: Boolean = true,
) = LocalDateTime.Format {
  date(
    LocalDate.Format {
      if (includeWeekDay) {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        chars(", ")
      }
      dayOfMonth()
      char(' ')
      monthName(MonthNames.ENGLISH_ABBREVIATED)
      char(' ')
      year()
    }
  )
}.format(date)

fun EventDetail.getDisplayDate(): String {
  if (beginDate.dayOfMonth == endDate.dayOfMonth) {
    val date = formatDatePretty(beginDate)
    val beginTime = extractTime(beginDate)
    val endTime = extractTime(endDate)

    return "$date at $beginTime - $endTime"
  } else {
    val beginDate = formatDatePretty(beginDate, includeWeekDay = false)
    val endDate = formatDatePretty(endDate, includeWeekDay = false)

    return "$beginDate - $endDate"
  }
}

fun EventDetail.getRemainingQuota(): String {
  return "${quota - registrants} remaining"
}

fun EventDetail.toPreview(): EventPreview {
  return EventPreview(
    id = id,
    name = name,
    summary = summary,
    cityName = cityName,
    imageLogo = imageLogo,
    isFavorite = isFavorite,
  )
}