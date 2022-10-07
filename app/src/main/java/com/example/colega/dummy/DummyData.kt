package com.example.colega.dummy

import com.example.colega.R
import com.example.colega.data.OnBoarding

class DummyData {
    companion object{
        private const val longString = "Michael Burry identified a market bubble of unprecedented size and scope last summer and warned investors buying into the hype and speculation to brace for a historic crash.\n" +
                "The fund manager of \"The Big Short\" fame said in a tweet this week that he believes the cataclysmic collapse is now in full swing and added it to his list of correct predictions he's made.\n" +
                "\"Crypo crash. Check,\" he tweeted. \"Meme crash. Check. SPAC crash. Check. Inflation. Check. 2000. Check. 2008. Check. 2022. Check.\"\n" +
                "The benchmark S&P 500 index has slumped by 19% this year, the tech-heavy Nasdaq has plunged by 27%, and the price of bitcoin has tanked by more than 66% from its November peak, to below \$19,000. However, Burry cautioned at the end of June, when asset prices hit similar levels, that the crash might be only halfway over based on how past downturns have played out.\n" +
                "\"No, we have not hit bottom yet,\" he tweeted on Wednesday. \"Watch for failure, then look for the bottom,\" he added, referring to companies and funds going under.\n" +
                "Many of the most popular meme stocks have plummeted in price this year. GameStop has slid by 34%, AMC Entertainment has dropped by 69%, and Bed Bath & Beyond has retreated by 54%. It's a similar story with the leading special-purpose acquisition companies; Virgin Galactic shares have fallen by 57% this year, while Lucid shares have tumbled by 64%.\n" +
                "Burry repeatedly said the booms in meme stocks, crypto, SPACs, and other trendy assets during the pandemic wouldn't last.\n" +
                "\"Fads today (#BTC, #EV, SaaS #memestocks) are like housing in 2007 and fiber in 1999,\" he tweeted in early 2021. \"On the whole, not wrong, just driven by speculative fervor to insane heights from which the fall will be dramatic and painful.\"\n" +
                "The Scion Asset Management boss highlighted the risk of inflation as early as April 2020. He also called bitcoin a debt-fueled \"speculative bubble\" and bemoaned the zealotry and aggressive promotion surrounding meme stocks and crypto.\n" +
                "Burry shot to fame after his billion-dollar bet against the US housing bubble in the mid-2000s was immortalized in the book and the movie \"The Big Short.\" He's also known for investing in GameStop before it gained meme-stock status and taking short positions against Elon Musk's Tesla and Cathie Wood's Ark Invest last year.\n" +
                "The Scion chief has likely escaped some of the market fallout this year, as he sold all but one of the stocks in his fund's portfolio during the second quarter."
        val onBoardingItems = arrayListOf(
            OnBoarding(img = R.drawable.ic_on_boarding_1, title = "Get your update news ", desc = "Lorem ipsum dolor sit amet, \n" +
                    "consectetur adipiscing elit. \n" +
                    "Cursus ornare eros convallis \n" +
                    "tellus vel et mi, gravida. \n" +
                    "Laoreet gravida metus at amet,\n" +
                    "Laoreet gravida metus at amet,\n"),
            OnBoarding(img = R.drawable.ic_on_boarding_1, title = "Get your update news ", desc = "Lorem ipsum dolor sit amet, \n" +
                    "consectetur adipiscing elit. \n" +
                    "Cursus ornare eros convallis \n" +
                    "tellus vel et mi, gravida. \n" +
                    "Laoreet gravida metus at amet,\n" +
                    "Laoreet gravida metus at amet,\n"),
            OnBoarding(img = R.drawable.ic_on_boarding_1, title = "Get your update news ", desc = "Lorem ipsum dolor sit amet, \n" +
                    "consectetur adipiscing elit. \n" +
                    "Cursus ornare eros convallis \n" +
                    "tellus vel et mi, gravida. \n" +
                    "Laoreet gravida metus at amet,\n" +
                    "Laoreet gravida metus at amet,\n"),
        )
    }
}