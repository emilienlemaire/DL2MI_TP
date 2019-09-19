# include "object.hpp"
#include <iostream>

void check_color(vec3 & color)
{
	color = max(color, vec3(0.0, 0.0, 0.0));
	color = min(color, vec3(1.0, 1.0, 1.0));
}

void Object::update_transformation(mat4 m)
{
    vertices_transformed = vertices;

    for (int i = 0; i < vertices_transformed.size(); ++i) {
        vertices_transformed[i] = (m * vertices.at(i));
        //if(i%100 == 0 ) std::cout << vertices_transformed[i][0] << " " << vertices_transformed[i][1] << " " <<vertices_transformed[i][2] << " " << vertices_transformed[i][3] << std::endl;
    }

    for (int i = 0; i < normals.size(); ++i) {
        normals_transformed.push_back(m * normals.at(i));
    }
    //std::cout << "********************************************************************" << std::endl;
}

void Object::update_projection(mat4 m)
{
    while(vertices_projected.size() < vertices_transformed.size()){
        vertices_projected.push_back(vec2(0));
    }

    for (int i = 0; i < vertices_projected.size(); ++i) {
        vec4 vec = m * vertices_transformed.at(i);
        vertices_projected.at(i) = (vec2(vec.x, vec.y));
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
                    window.draw_line(vertices_projected[faces[i].vertex_index[0]], vertices_projected[faces[i].vertex_index[1]], faces[i].color);
                    window.draw_line(vertices_projected[faces[i].vertex_index[1]], vertices_projected[faces[i].vertex_index[2]], faces[i].color);
                    window.draw_line(vertices_projected[faces[i].vertex_index[2]], vertices_projected[faces[i].vertex_index[3]], faces[i].color);
                    window.draw_line(vertices_projected[faces[i].vertex_index[3]], vertices_projected[faces[i].vertex_index[0]], faces[i].color);
				}
				break;
			case DRAW_FILL :
				// TODO => TP03 //
				break;
			case DRAW_LAMBERT :
				// TODO => TP04 //
				break;
			case DRAW_GOURAUD :
				// TODO => TP04 //
				break;
			default :
				break;
		}
	}
}

void Object::backface_culling(mat4 trans_matrix)
{
	// TODO => TP03 //
}
