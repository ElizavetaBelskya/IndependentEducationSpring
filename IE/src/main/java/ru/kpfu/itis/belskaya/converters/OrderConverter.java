package ru.kpfu.itis.belskaya.converters;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kpfu.itis.belskaya.models.Gender;
import ru.kpfu.itis.belskaya.models.Order;
import ru.kpfu.itis.belskaya.models.Student;
import ru.kpfu.itis.belskaya.models.Tutor;
import ru.kpfu.itis.belskaya.models.dto.OrderDto;
import ru.kpfu.itis.belskaya.models.forms.OrderForm;
import ru.kpfu.itis.belskaya.services.OrderService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Elizaveta Belskaya
 */

@AllArgsConstructor
public class OrderConverter implements GenericConverter {

    private Student author;


    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        HashSet<ConvertiblePair> pairSet = new HashSet<>();
        pairSet.add(new ConvertiblePair(OrderForm.class, Order.class));
        pairSet.add(new ConvertiblePair(Order.class, OrderDto.class));
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
        } else if (Order.class.equals(typeDescriptor.getType()) && OrderDto.class.equals(typeDescriptor1.getType())) {
            Order order = (Order) o;
            OrderDto orderDto = OrderDto.builder().id(order.getId())
                    .creationDate(order.getCreationDate())
                    .candidates(order.getCandidates().stream().mapToLong(Tutor::getId).boxed().collect(Collectors.toList()))
                    .studentId(order.getAuthor().getId())
                    .tutorId(order.getTutor() == null ? null : order.getTutor().getId())
                    .price(order.getPrice())
                    .description(order.getDescription())
                    .minRating(order.getMinRating())
                    .tutorGender(order.getTutorGender().toString())
                    .online(order.getOnline().toString())
                    .subject(order.getSubject()).build();
            return orderDto;
        } else if (OrderDto.class.equals(typeDescriptor.getType()) && Order.class.equals(typeDescriptor1.getType())) {
            OrderDto orderDto = (OrderDto) o;
            Order order = Order.builder().author(author)
                    .price(orderDto.getPrice())
                    .description(orderDto.getDescription())
                    .minRating(orderDto.getMinRating())
                    .tutorGender(Gender.valueOf(orderDto.getTutorGender()))
                    .online(Order.Format.valueOf(orderDto.getOnline()))
                    .subject(orderDto.getSubject()).build();
            return order;
        }
        throw new IllegalArgumentException();
    }


}
