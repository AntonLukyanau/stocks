package com.alukyanau.nysestocks.service;

import java.util.List;

public interface Parser<T, W> {

    List<T> parse(W source);

}
