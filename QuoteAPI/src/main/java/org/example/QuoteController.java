package org.example;

import org.example.model.Quote;
import org.example.model.QuoteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/quote")
public class QuoteController {

    private final List<Quote> quotes = new ArrayList<>();

    public QuoteController() {
        quotes.add(new Quote(1, "Your heart is the size of an ocean. Go find yourself in its hidden depths.", "Rumi"));
        quotes.add(new Quote(2, "The Bay of Bengal is hit frequently by cyclones. The months of November and May, in particular, are dangerous in this regard.", "Abdul Kalam"));
        quotes.add(new Quote(3, "Thinking is the capital, Enterprise is the way, Hard Work is the solution.", "Abdul Kalam"));
        quotes.add(new Quote(4, "If You Can't Make It Good, At Least Make It Look Good.", "Bill Gates"));
        quotes.add(new Quote(5, "Heart be brave. If you cannot be brave, just go. Love's glory is not a small thing.", "Rumi"));
        quotes.add(new Quote(6, "It is bad for a young man to sin; but it is worse for an old man to sin.", "Abu Bakr (R.A)"));
        quotes.add(new Quote(7, "If You Are Out To Describe The Truth, Leave Elegance To The Tailor.", "Albert Einstein"));
        quotes.add(new Quote(8, "O man you are busy working for the world, and the world is busy trying to turn you out.", "Abu Bakr (R.A)"));
        quotes.add(new Quote(9, "While children are struggling to be unique, the world around them is trying all means to make them look like everybody else.", "Abdul Kalam"));
        quotes.add(new Quote(10, "These Capitalists Generally Act Harmoniously And In Concert, To Fleece The People.", "Abraham Lincoln"));
        quotes.add(new Quote(11, "I Don't Believe In Failure. It Is Not Failure If You Enjoyed The Process.", "Oprah Winfrey"));
        quotes.add(new Quote(12, "Do not get elated at any victory, for all such victory is subject to the will of God.", "Abu Bakr (R.A)"));
        quotes.add(new Quote(13, "Wear gratitude like a cloak and it will feed every corner of your life.", "Rumi"));
        quotes.add(new Quote(14, "If you even dream of beating me you'd better wake up and apologize.", "Muhammad Ali"));
        quotes.add(new Quote(15, "I Will Praise Any Man That Will Praise Me.", "William Shakespeare"));
        quotes.add(new Quote(16, "One Of The Greatest Diseases Is To Be Nobody To Anybody.", "Mother Teresa"));
        quotes.add(new Quote(17, "I'm so fast that last night I turned off the light switch in my hotel room and was in bed before the room was dark.", "Muhammad Ali"));
        quotes.add(new Quote(18, "People Must Learn To Hate And If They Can Learn To Hate, They Can Be Taught To Love.", "Nelson Mandela"));
        quotes.add(new Quote(19, "Everyone has been made for some particular work, and the desire for that work has been put in every heart.", "Rumi"));
        quotes.add(new Quote(20, "The less of the World, the freer you live.", "Umar ibn Al-Khattāb (R.A)"));
        quotes.add(new Quote(21, "Respond to every call that excites your spirit.", "Rumi"));
        quotes.add(new Quote(22, "The Way To Get Started Is To Quit Talking And Begin Doing.", "Walt Disney"));
        quotes.add(new Quote(23, "God Doesn't Require Us To Succeed, He Only Requires That You Try.", "Mother Teresa"));
        quotes.add(new Quote(24, "Speak any language, Turkish, Greek, Persian, Arabic, but always speak with love.", "Rumi"));
        quotes.add(new Quote(25, "Happiness comes towards those which believe in him.", "Ali ibn Abi Talib (R.A)"));
        quotes.add(new Quote(26, "Knowledge is of two kinds: that which is absorbed and that which is heard. And that which is heard does not profit if it is not absorbed.", "Ali ibn Abi Talib (R.A)"));
        quotes.add(new Quote(27, "When I am silent, I have thunder hidden inside.", "Rumi"));
        quotes.add(new Quote(28, "Technological Progress Is Like An Axe In The Hands Of A Pathological Criminal.", "Albert Einstein"));
        quotes.add(new Quote(29, "No One Would Choose A Friendless Existence On Condition Of Having All The Other Things In The World.", "Aristotle"));
        quotes.add(new Quote(30, "Life is a gamble. You can get hurt, but people die in plane crashes, lose their arms and legs in car accidents; people die every day. Same with fighters: some die, some get hurt, some go on. You just don't let yourself believe it will happen to you.", "Muhammad Ali"));
    }

    @GetMapping("/{id}")
    public Quote getQuote(@PathVariable("id") int id) throws InterruptedException {
        Thread.sleep(ThreadLocalRandom.current().nextInt(200, 500));
        return quotes.stream()
                .filter(q -> q.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quote não encontrado"));
    }

    @GetMapping
    public QuoteResponse getAllQuotes() throws InterruptedException {
        Thread.sleep(300);
        return new QuoteResponse(quotes, 1454, 0, 30);
    }
}
