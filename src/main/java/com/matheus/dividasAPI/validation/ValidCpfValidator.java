package com.matheus.dividasAPI.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context){

        if (cpf == null){
            return true;
        }

        if (!cpf.matches("\\d{11}")){
            return false;
        }

        // Rejeita cpf com todos digitos iguais
        if (cpf.chars().distinct().count() == 1){
            return false;
        }

        int soma = 0;

        for (int i = 0; i < 9; i++){
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * (10 - i);
        }

        int resto = soma % 11;

        int primeiroDigito = resto < 2 ? 0 : 11 - resto;

        int digitoInformado = Character.getNumericValue(cpf.charAt(9));

        if (primeiroDigito != digitoInformado){
            return false;
        }

        soma = 0;

        for(int i = 0; i < 10; i++){
            int digito = Character.getNumericValue(cpf.charAt(i));
            soma += digito * (11 - i);
        }

        resto = soma % 11;

        int segundoDigito = resto < 2 ? 0 : 11 - resto;

        int segundoDigitoInformado = Character.getNumericValue(cpf.charAt(10));

        return segundoDigito == segundoDigitoInformado;

    }


}
