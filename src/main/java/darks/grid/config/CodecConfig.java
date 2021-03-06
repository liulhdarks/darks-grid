/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package darks.grid.config;

import java.util.HashMap;
import java.util.Map;

import darks.grid.network.codec.GridCodec;

public class CodecConfig 
{
    
    
    
    private String type;
    
    private Class<? extends GridCodec> codecClass = null;
    
    private Map<String, String> parameters = new HashMap<String, String>();
    
    public CodecConfig()
    {
        
    }
    
    public void addParameter(String key, String value) 
    {
        parameters.put(key, value);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public Class<? extends GridCodec> getCodecClass() {
        return codecClass;
    }

    public void setCodecClass(Class<? extends GridCodec> codecClass) {
        this.codecClass = codecClass;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
    

}
