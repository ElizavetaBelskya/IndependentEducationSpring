package ru.kpfu.itis.belskaya.converters;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.belskaya.models.Gender;
import ru.kpfu.itis.belskaya.models.Order;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.forms.OrderForm;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Elizaveta Belskaya
 */

@AllArgsConstructor
public class OrderFormToOrderConverter implements GenericConverter {

    private Student author;
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        HashSet<ConvertiblePair> pairSet = new HashSet<>();
        pairSet.add(new ConvertiblePair(OrderForm.class, Order.class));
        return pairSet;
    }

    @Override
    public Object convert(Object o, TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor1) {
        if (OrderForm.class.equals(typeDescriptor.getType()) && Order.class.equals(typeDescriptor1.getType())) {
            OrderForm src = (OrderForm) o;
            Order order = Order.builder()
                    .description(src.getDescription())
                    .subject(src.getSubject())
                    .minRating(src.getRating()? 4.0f:0.0f)
                    .price(src.getPrice())
                    .online(Enum.valueOf(Order.Format.class, src.getOnline().toUpperCase()))
                    .tutorGender(Enum.valueOf(Gender.class, src.getGender().toUpperCase()))
                    .author(author)
                    .build();
            return order;
        }
        throw new IllegalArgumentException();
    }


}
