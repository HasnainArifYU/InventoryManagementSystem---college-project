package husnain.ims.app.sample.data;

import com.github.javafaker.Faker;
import husnain.ims.app.crud.Inventory;
import husnain.ims.app.crud.utils.IdSequence;
import husnain.ims.app.model.InHouse;
import husnain.ims.app.model.OutSourced;
import husnain.ims.app.model.Part;
import husnain.ims.app.model.Product;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Husnain Arif
 */
public class Generate {

    private static final IdSequence SEQ = IdSequence.getInstance();

    public Generate() {
    }

    public void sampleData() {
        var someParts = Stream.generate(() -> Generate.somePart())
                .limit(100)
                .collect(Collectors.toList());

        someParts.stream().forEachOrdered(Inventory::addPart);

        Stream.generate(() -> Generate.someProduct(someParts))
                .limit(50)
                .forEachOrdered(Inventory::addProduct);
    }

    private static Part somePart() {
        Part part;

        var faker = new Faker();
        var bool = faker.bool();
        var random = faker.random();
        var commerce = faker.commerce();

        if (bool.bool() && bool.bool()) {
            part = new InHouse(SEQ.next(), commerce.productName(), Double.parseDouble(commerce.price()), random.nextInt(10, 100), 10, 100);

            ((InHouse) part).setMachineId(random.nextInt(1000));
        } else {
            part = new OutSourced(SEQ.next(), commerce.productName(), Double.parseDouble(commerce.price()), random.nextInt(10, 100), 10, 100);

            ((OutSourced) part).setCompanyName(faker.company().name());
        }

        return part;
    }

    private static Product someProduct(List<Part> parts) {
        Product product;

        var faker = new Faker();
        var commerce = faker.commerce();
        var random = faker.random();

        product = new Product(SEQ.next(), commerce.productName(), Double.parseDouble(commerce.price()), random.nextInt(10, 100), 10, 100);

        Collections.shuffle(parts);

        parts.stream().limit(5).forEachOrdered(product::addAssociatedPart);

        return product;
    }
}
