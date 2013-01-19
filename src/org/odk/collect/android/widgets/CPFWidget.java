/*
 * Copyright (C) 2009 University of Washington
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.odk.collect.android.widgets;

import java.util.InputMismatchException;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.StringData;
import org.javarosa.form.api.FormEntryPrompt;

import android.content.Context;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.TypedValue;



/**
 * Widget that restricts values to integers.
 * 
 * @author Carl Hartung (carlhartung@gmail.com)
 */
public class CPFWidget extends StringWidget {
	

	public CPFWidget(Context context, FormEntryPrompt prompt) {
        super(context, prompt, true);

        mAnswer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
        mAnswer.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);

        // needed to make long readonly text scroll
        mAnswer.setHorizontallyScrolling(false);
        mAnswer.setSingleLine(false);

        mAnswer.setKeyListener(new DigitsKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] accepted = {
                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
                };
                return accepted;
            }
        });

        if (prompt.isReadOnly()) {
            setBackgroundDrawable(null);
            setFocusable(false);
            setClickable(false);
        }

        String s = null;
        if (prompt.getAnswerValue() != null)
            s = (String) prompt.getAnswerValue().getValue();

        if (s != null) {
            mAnswer.setText(s);
        }
        
        setupChangeListener();
    }


    @Override
    public IAnswerData getAnswer() {
    	clearFocus();
    	String s = mAnswer.getText().toString();
        if (s == null || s.equals("")) {
            return null;
        } else {
            try {
               
            	if (s.equals("00000000000") ||
            	    s.equals("22222222222") || s.equals("33333333333") ||
            	    s.equals("44444444444") || s.equals("55555555555") ||
            	    s.equals("66666666666") || s.equals("77777777777") ||
            	    s.equals("88888888888") || s.equals("99999999999") ||
            	   (s.length() != 11)) {
            		s = "1";
            	}

            	char dig10, dig11;
                int sm, i, r, num, peso;
            
            	
            	try { // Esse try é para error de conversão
            		
            		
            		// Calculo do 1o. Digito Verificador
            	      sm = 0;
            	      peso = 10;
            	      for (i=0; i<9; i++) {             
            	      // converte o i-esimo caractere do CPF em um numero:
                      // por exemplo, transforma o caractere '0' no inteiro 0        
            	      // (48 eh a posicao de '0' na tabela ASCII)        
            	        num = (int)(s.charAt(i) - 48);
            	        sm = sm + (num * peso);
            	        peso = peso - 1;
            	      }
            	 
            	      r = 11 - (sm % 11);
            	      if ((r == 10) || (r == 11))
            	         dig10 = '0';
            	      else dig10 = (char)(r + 48); // converte no respectivo caractere numerico
            	 
            	// Calculo do 2o. Digito Verificador
            	      sm = 0;
            	      peso = 11;
            	      for(i=0; i<10; i++) {
            	        num = (int)(s.charAt(i) - 48);
            	        sm = sm + (num * peso);
            	        peso = peso - 1;
            	      }
            	 
            	      r = 11 - (sm % 11);
            	      if ((r == 10) || (r == 11))
            	         dig11 = '0';
            	      else dig11 = (char)(r + 48);
            	 
            	// Verifica se os digitos calculados conferem com os digitos informados.
            	      if ((dig10 != s.charAt(9)) || (dig11 != s.charAt(10))) {
            	        // return null ;
            	    	  s = "1"; // Vou utilizar assim para poder usar regex com o CPF, assim uso constraint e readonly ou só constraint e irá funcionar
            	      }
            	      
            	} catch (InputMismatchException erro) {
                    s = "1";
                }
            	
            	return new StringData(s);
            	
            } catch (Exception NumberFormatException) {
            	 s = "1";
            	 return new StringData(s);
            }
        }
    }

}
