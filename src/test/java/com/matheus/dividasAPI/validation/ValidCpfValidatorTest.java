package com.matheus.dividasAPI.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidCpfValidatorTest {

    private final ValidCpfValidator validator = new ValidCpfValidator();

    @Test
    void deveAceitarCpfValido(){
        assertTrue(validator.isValid("52998224725", null));
    }

    @Test
    void deveRejeitarCpfComDigitoVerificadorInvalido(){
        assertFalse(validator.isValid("52998224726", null));
    }

    @Test
    void deveRejeitarCpfComTodosOsDigitosIguais(){
        assertFalse(validator.isValid("11111111111", null));
    }

    @Test
    void deveRejeitarCpfComQuantidadeDeDigitosInvalida(){
        assertFalse(validator.isValid("5299822472", null));
    }

    @Test
    void deveAceitarValorNulo(){
        assertTrue(validator.isValid(null, null));
    }


}
