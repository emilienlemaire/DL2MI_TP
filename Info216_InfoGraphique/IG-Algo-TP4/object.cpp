# include "object.hpp"

void check_color(vec3 & color)
{
	color = max(color, vec3(0.0, 0.0, 0.0));
	color = min(color, vec3(1.0, 1.0, 1.0));
}

void Object::update_transformation(mat4 m)
{
	vec4 v_out, n_out;
	vertices_transformed.clear();
	for(unsigned int i=0; i<vertices.size(); i++)
	{
		v_out = m * vertices[i];
		vertices_transformed.push_back(v_out);
	}
	for(unsigned int i=0; i<faces.size(); i++)
	{
		faces[i].normal_transformed = m * faces[i].normal;
	}
	normals_transformed.clear();
	for(unsigned int i=0; i<normals.size(); i++)
	{
		n_out = m * normals[i];
		normals_transformed.push_back(n_out);
	}
}

void Object::update_projection(mat4x3 m)
{
	vec3 v_out;
	vertices_projected.clear();
	for(unsigned int i=0; i<vertices_transformed.size(); i++)
	{
		v_out = m * vertices_transformed[i];
		vertices_projected.push_back(vec2(v_out/v_out.z));
	}
}

void Object::draw(Window & window, vec4 light)
{
	for(unsigned int i=0; i<faces.size(); i++)
	{
		switch(draw_method)
		{
			case DRAW_WIRE :
				{
					for(unsigned int v=0; v<4; v++)
					{
						vec2 p1 = vertices_projected[faces[i].vertex_index[v]];
						vec2 p2 = vertices_projected[faces[i].vertex_index[(v+1)%4]];
						window.draw_line(p1, p2, faces[i].color);
					}
				}
				break;
			case DRAW_FILL :
				if(faces[i].visible)
				{
					vec2 p[4];
					for(unsigned int v=0; v<4; v++)
					{
						p[v] = vertices_projected[faces[i].vertex_index[v]];
					}
					window.draw_quad(p, faces[i].color);
				}
				break;
			case DRAW_LAMBERT :
			    if(faces[i].visible){
                    float scalar = dot(faces[i].normal_transformed, light);
			        vec3 color(faces[i].color * scalar);
					vec2 p[4];
					for(unsigned int v=0; v<4; v++)
					{
						p[v] = vertices_projected[faces[i].vertex_index[v]];
					}
					window.draw_quad(p, color);
			    }
				break;
			case DRAW_GOURAUD :
			    if(faces[i].visible){
			        vec3 c[4];
					vec2 p[4];
                    vec3 colors[4];
					for(unsigned int v=0; v<4; v++)
					{
                        int facesInd[4];
                        vec4 normalP;

                        vec4 somme = vec4(0);

                        for (int n = 0; n < 4; ++n) {
                            somme += faces[(i+n) % faces.size()].normal_transformed;
                        }

                        vec4 sommeAbs = abs(somme);

                        normalP = somme / sommeAbs;
                        float scalar = dot(normalP, light);
                        colors[v] = faces[i].color * scalar;
						p[v] = vertices_projected[faces[i].vertex_index[v]];
					}
					window.draw_quad(p, colors);
			    }
				break;
			default :
				break;
		}
	}
}

void Object::backface_culling()
{
	float dot_product = 0;
	vec4 current_vertex, current_normal;
	for(unsigned int i=0; i<faces.size(); i++)
	{
		current_vertex = vertices_transformed[faces[i].vertex_index[0]];
		dot_product = dot(current_vertex, faces[i].normal_transformed);
		if(dot_product < 0.0f)
		{
			faces[i].visible = 1;
		}
		else
		{
			faces[i].visible = 0;
		}
	}
}
