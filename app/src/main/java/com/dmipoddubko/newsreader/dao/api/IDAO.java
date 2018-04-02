package com.dmipoddubko.newsreader.dao.api;

import java.util.List;

public interface IDAO<E> {

    int insert(E e);

    int update(E e);

    int delete(int id);

    int delete(E e);

    int deleteAll();

    List<E> findAll();

    E findById(int id);
}
