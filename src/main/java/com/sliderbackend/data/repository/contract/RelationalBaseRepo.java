package com.sliderbackend.data.repository.contract;

import com.sliderbackend.data.repository.BaseModel;
import com.sliderbackend.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
public abstract class RelationalBaseRepo<T extends BaseModel, R extends MongoRepository<T, String>> implements IRepo<T> {

    private final R r;
    private final String name;


    public RelationalBaseRepo(R r, String name) {
        this.r = r;
        this.name = name;
    }

    public T create(T t) {
        return r.save(t);
    }

    public T update(T t) {
        if (r.findById(t.getId()).isEmpty())
            throw new ResourceNotFoundException(name + " not found");

        return r.save(t);
    }

    public Page<T> getAll(int pageNum, int pageSize) {
        List<T> ts;

        if (pageNum > 0) {
            return r.findAll(PageRequest.of(pageNum - 1, pageSize, Sort.by(new Sort.Order(Sort.Direction.DESC, "id"))));
        } else {
            ts = r.findAll(Sort.by(new Sort.Order(Sort.Direction.DESC, "id")));
            return new PageImpl<>(ts);
        }
    }

    public T getById(String id) {
        Optional<T> t = r.findById(id);

        if (t.isEmpty())
            throw new ResourceNotFoundException(format("%s with id %s not found", name, id));

        return t.get();
    }

    public void delete(String id) {
        if (r.findById(id).isEmpty())
            throw new ResourceNotFoundException(name + " not found");

        r.deleteById(id);
    }

    public void deleteAll(List<T> ts) {
        r.deleteAll(ts);
    }
}