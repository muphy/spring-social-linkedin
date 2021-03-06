/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.linkedin.api.impl.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.social.linkedin.api.LinkedInProfile;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class LikesListDeserializer extends JsonDeserializer<List<LinkedInProfile>> {

	@Override
	public List<LinkedInProfile> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new LinkedInModule());
		jp.setCodec(mapper);
		if(jp.hasCurrentToken()) {
			JsonNode dataNode = jp.readValueAs(JsonNode.class).get("values");
			List<LinkedInProfile> likes = new ArrayList<LinkedInProfile>();
			// Have to iterate through list due to person sub object.
			for (JsonNode like : dataNode) {
				LinkedInProfile profile = mapper.reader(new TypeReference<LinkedInProfile>() {}).readValue(like.get("person"));
				likes.add(profile);
			}
			return likes;
		}
		
		return null;
	}

}
