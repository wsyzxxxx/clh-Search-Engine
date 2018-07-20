package com.searcher.searcher.Spider;

import com.searcher.searcher.DataFormat.WebPageData;

import java.io.IOException;
import java.util.Vector;

public interface Spider {
    Vector<WebPageData> getData() throws IOException;
}
