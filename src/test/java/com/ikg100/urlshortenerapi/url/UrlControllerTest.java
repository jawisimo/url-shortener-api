package com.ikg100.urlshortenerapi.url;

import com.ikg100.urlshortenerapi.url.controller.UrlController;
import com.ikg100.urlshortenerapi.url.dto.operations.GetShortUrlRequest;
import com.ikg100.urlshortenerapi.url.dto.operations.UpdateUrlRequest;
import com.ikg100.urlshortenerapi.url.dto.operations.UrlResponse;
import com.ikg100.urlshortenerapi.url.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlControllerTest {

    @Mock
    private UrlService urlService;

    @InjectMocks
    private UrlController urlController;

    private String shortUrlCode;

    @BeforeEach
    void init() {
        shortUrlCode = "abc123";
    }

    @Test
    void shortFromLong_shouldReturnCreatedAndResponseBody_withSuccessRequest() {
        GetShortUrlRequest request = new GetShortUrlRequest();
        UrlResponse response = new UrlResponse();
        when(urlService.getShortUrlCodeFromLongUrl(request)).thenReturn(response);
        ResponseEntity<UrlResponse> result = urlController.shortFromLong(request);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(urlService).getShortUrlCodeFromLongUrl(request);
    }

    @Test
    void shortFromLong_shouldThrowException_withInvalidRequest() {
        GetShortUrlRequest request = new GetShortUrlRequest();
        when(urlService.getShortUrlCodeFromLongUrl(request))
                .thenThrow(new IllegalArgumentException("Invalid URL"));
        assertThrows(IllegalArgumentException.class, () -> urlController.shortFromLong(request));
        verify(urlService).getShortUrlCodeFromLongUrl(request);
    }

    @Test
    void longFromShort_shouldReturnOkAndResponseBody_withSuccessRequest() {
        UrlResponse response = new UrlResponse();
        when(urlService.getLongUrlFromShortUrl(shortUrlCode)).thenReturn(response);
        ResponseEntity<UrlResponse> result = urlController.longFromShort(shortUrlCode);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(urlService).getLongUrlFromShortUrl(shortUrlCode);
    }

    @Test
    void longFromShort_shouldThrowException_withInvalidRequest() {
        when(urlService.getLongUrlFromShortUrl(shortUrlCode))
                .thenThrow(new IllegalArgumentException("Invalid URL"));
        assertThrows(IllegalArgumentException.class, () -> urlController.longFromShort(shortUrlCode));
        verify(urlService).getLongUrlFromShortUrl(shortUrlCode);
    }

    @Test
    void updateUrl_shouldReturnOkAndResponseBody_withSuccessRequest() {
        UpdateUrlRequest request = new UpdateUrlRequest();
        UrlResponse response = new UrlResponse();
        when(urlService.updateUrl(request)).thenReturn(response);
        ResponseEntity<UrlResponse> result = urlController.updateUrl(request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(urlService).updateUrl(request);
    }

    @Test
    void updateUrl_shouldThrowException_withInvalidRequest() {
        UpdateUrlRequest request = new UpdateUrlRequest();
        when(urlService.updateUrl(request))
                .thenThrow(new IllegalArgumentException("Invalid URL"));
        assertThrows(IllegalArgumentException.class, () -> urlController.updateUrl(request));
        verify(urlService).updateUrl(request);
    }

    @Test
    void deleteUrl_shouldReturnNoContent_withSuccessRequest() {
        doNothing().when(urlService).deleteUrl(shortUrlCode);
        ResponseEntity<UrlResponse> result = urlController.deleteUrl(shortUrlCode);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(urlService).deleteUrl(shortUrlCode);
    }

    @Test
    void deleteUrl_shouldThrowException_withInvalidRequest() {
        doThrow(new IllegalArgumentException("Invalid URL")).when(urlService).deleteUrl(shortUrlCode);
        assertThrows(IllegalArgumentException.class, () -> urlController.deleteUrl(shortUrlCode));
        verify(urlService).deleteUrl(shortUrlCode);
    }
}
