package hello.itemservicedb.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservicedb.domain.Item;
import hello.itemservicedb.domain.QItem;
import hello.itemservicedb.repository.ItemRepository;
import hello.itemservicedb.repository.ItemSearchCond;
import hello.itemservicedb.repository.ItemUpdateDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static hello.itemservicedb.domain.QItem.*;

@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;
    private final SpringDataJpaItemRepository repository;

    public JpaItemRepositoryV3(EntityManager em, SpringDataJpaItemRepository repository) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
        this.repository = repository;
    }

    @Override
    public Item save(Item item) {
//        em.persist(item);
//        return item;
        return repository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
//        Item findItem = em.find(Item.class, itemId);
//        findItem.setItemName(updateParam.getItemName());
//        findItem.setPrice(updateParam.getPrice());
//        findItem.setQuantity(updateParam.getQuantity());

        Item findItem = repository.findById(itemId).orElseThrow();
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice((updateParam.getPrice()));
        findItem.setQuantity(updateParam.getQuantity());
    } // 트랜잭션 커밋 시점에 반영 됨.

    @Override
    public Optional<Item> findById(Long id) {
//        Item item = em.find(Item.class, id);
//        return Optional.ofNullable(item);
        return repository.findById(id);
    }

//    @Override
    public List<Item> findAllOld(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

//        if (StringUtils.hasText(itemName) && maxPrice != null) {
////            return repository.findByItemNameLikeAndPriceLessThanEqual(itemName, maxPrice);
//            return repository.findItems(itemName, maxPrice);
//        } else if (StringUtils.hasText(itemName)) {
//            return repository.findByItemNameLike(itemName);
//        } else if (maxPrice != null) {
//            return repository.findByPriceLessThanEqual(maxPrice);
//        } else {
//            return repository.findAll();
//        }

        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(itemName)) {
            builder.and(item.itemName.like("%" + itemName + "%"));
        }
        if (maxPrice != null) {
            builder.and(item.price.loe(maxPrice));
        }

        List<Item> result = query
                .select(item)
                .from(item)
                .where(builder)
                .fetch();

        return result;
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        return query
                .select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetch();
    }

    private BooleanExpression likeItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if (maxPrice != null) {
            return item.price.loe(maxPrice);
        }
        return null;
    }
}
