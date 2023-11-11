package ktplayground

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import org.slf4j.LoggerFactory

object Application {
    private val log = LoggerFactory.getLogger(Application::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val playwright = Playwright.create()
        val browser = playwright.chromium().launch(
            BrowserType.LaunchOptions()
                .setHeadless(false)
        )
        val context = browser.newContext()
        val page = context.newPage()

        page.navigate("https://hotels.naver.com/item/rates?hotelFileName=hotel%3AThe_Palazzo_at_The_Venetian_r&adultCnt=2&checkIn=2024-01-19&checkOut=2024-01-22&includeTax=true")

        log.info("Some Log Message!")
        println("Some Random Message!")
    }
}
