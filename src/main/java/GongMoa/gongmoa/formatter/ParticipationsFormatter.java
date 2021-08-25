package GongMoa.gongmoa.formatter;

import GongMoa.gongmoa.domain.Participation;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ParticipationsFormatter implements Formatter<List<Participation>> {
    @Override
    public List<Participation> parse(String text, Locale locale) throws ParseException {
        throw new ParseException("해당 포맷팅을 지원하지 않습니다", 0);
    }

    @Override
    public String print(List<Participation> object, Locale locale) {
        StringBuffer sb = new StringBuffer();
        object.forEach(p -> sb.append(p.getUser().getName()).append(", "));

        sb.delete(sb.length()-2, sb.length());

        return sb.toString();
    }
}
