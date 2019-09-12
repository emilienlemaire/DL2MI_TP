# include <limits>
# include <math.h>
# include "window.hpp"
# include <iostream>

# define MININT std::numeric_limits<int>::min()

Window::Window()
{
	pixels = NULL;
	pixels_final = NULL;
	// J'ai changé ces valeurs par rapport au prof
	width = 800;
	height = 800;
	sample = 1;
	width *= sample;
	height *= sample;
	nb_pixels = width*height;
}

Window::~Window()
{
	if (pixels != NULL)
	{
		delete[] pixels;
	}
	if(pixels_final != NULL)
	{
		delete[] pixels_final;
	}
}

const int Window::get_width()
{
	return width / sample;
}

const int Window::get_height()
{
	return height / sample;
}

unsigned char * Window::get_pixels()
{
	if(sample == 1)
	{
		return pixels;
	}
	else
	{
		return pixels_final;
	}
}
//
vec3 Window::get_pixel(vec2 p)
{
	if(0<=p.x && p.x<width && 0<=p.y && p.y<height)
	{
		int indice = (p.x + p.y*width) * 3;
		return vec3(c2f(pixels[indice]), c2f(pixels[indice+1]), c2f(pixels[indice+2]));
	}

	return vec3();
}

void Window::draw_pixel(vec2 p, vec3 c)
{
	if(p.x>=0 && p.x<width && p.y>=0 && p.y<height)
	{
		int indice = (p.x + p.y*width) * 3;
		pixels[indice] = f2c(c.x);
		pixels[indice+1] = f2c(c.y);
		pixels[indice+2] = f2c(c.z);
	}
}

void Window::draw_pixel_sampled(vec2 p, vec3 c)
{
	if(0<=p.x && p.x<width && 0<=p.y && p.y<height)
	{
		int indice = (p.x + p.y*width/sample) * 3;
		pixels_final[indice] = f2c(c.x);
		pixels_final[indice+1] = f2c(c.y);
		pixels_final[indice+2] = f2c(c.z);
	}
}

void Window::clear()
{
	//Clear color
	for(int i=0; i<nb_pixels*3; i+=3)
	{
		pixels[i] = 0;
		pixels[i+1] = 0;
		pixels[i+2] = 0;
	}
}

void Window::reshape(int w, int h)
{
	width = w;
	height = h;
	width *= sample;
	height *= sample;
	nb_pixels = width * height;

	if(pixels != NULL)
	{
		delete[] pixels;
	}
	if(pixels_final != NULL)
	{
		delete[] pixels_final;
	}

	pixels = new unsigned char[nb_pixels*3];
	pixels_final = new unsigned char[(nb_pixels/(sample*sample))*3];
	//Clear color
	for(int i=0; i<nb_pixels*3; i+=3)
	{
		pixels[i] = 0;
		pixels[i+1] = 0;
		pixels[i+2] = 0;
	}
}

void Window::antialiasing()
{
    /*
     *  ATTENTION: Algorithme du prof
     */
    vec3 color;
    for(int x = 0; x < width - sample; x+=sample){

        for (int y = 0; y < height - sample; y += sample) {

            for (int i = 0; i < sample; ++i) {

                for (int j = 0; j < sample; ++j) {

                    color += get_pixel(vec2(x+i, y+j));

                }
            }

            color /= sample * sample;
            draw_pixel_sampled(vec2(x/sample, y/sample), color);
            color = vec3();

        }
    }
}

void Window::draw_line(vec2 p1, vec2 p2, vec3 c)
{
    int dx = p2.x - p1.x;
    int dy = p2.y - p1.y;
    int x = p1.x;
    int y = p1.y;
    int e = 2 * dy - dx;
    if (dx == 0){
        if (dy > 0) {
            //dx == 0, dy > 0

            while (y < p2.y){
                draw_pixel(vec2(x, y), c);
                y += 1;
            }
        } else {
            //dx == 0, dy < 0

            while (y > p2.y){
                draw_pixel(vec2(x, y), c);
                y -= 1;
            }
        }
    } else if( dx > 0){
        if (dy == 0) {
            //dx > 0, dy == 0

            while(x < p2.x) {
                draw_pixel(vec2(x,y), c);
                x += 1;
            }
        } else if (dy > 0) {
            //dx > 0, dy > 0
            //Algo du cours

            int IncD = 2 * (dy - dx);
            int IncH = 2 * dy;

            while(x < p2.x){
                draw_pixel(vec2(x, y), c);
                x += 1;
                if (e > 0) {
                    y += 1;
                    e += IncD;
                } else {
                    //e < 0
                    e += IncH;
                }
            }
        } else {
            //dx > 0, dy < 0
            //Pour IncD et IncH on remplace dx par -dx

            int IncD = 2 * (-dy - dx);
            int IncH = 2 * -dy;

            while(x < p2.x){
                draw_pixel(vec2(x, y), c);
                x += 1;
                if (e > 0) {
                    y -= 1;
                    e += IncD;
                } else {
                    //e < 0
                    e += IncH;
                }
            }
        }
    } else {
        // dx < 0

        if (dy == 0){
            //dx < 0, dy == 0

            while(x > p2.x) {
                draw_pixel(vec2(x,y), c);
                x -= 1;
            }
        } else if (dy > 0){
            //dx < 0, dy > 0
            /*
             *   Pour IncD et IncH on remplace dx par -dx
             *   Pour x on fait -1 et x > p2.x
             */

            int IncD = 2 * (dy + dx);
            int IncH = 2 * dy;

            while(x > p2.x){
                draw_pixel(vec2(x, y), c);
                x -= 1;
                if (e > 0) {
                    y += 1;
                    e += IncD;
                } else {
                    //e < 0
                    e += IncH;
                }
            }
        } else {
            //dx < 0, dy < 0
            /*
             *   Pour IncD et IncH on remplace dx par -dx et dy par -dy
             *   Pour x on fait -1 et x > p2.x
             */

            int IncD = 2 * (-dy + dx);
            int IncH = 2 * -dy;

            while(x > p2.x){
                draw_pixel(vec2(x, y), c);
                x -= 1;
                if (e > 0) {
                    y -= 1;
                    e += IncD;
                } else {
                    //e < 0
                    e += IncH;
                }
            }
        }
    }
}

void Window::draw_circle(vec2 center, unsigned int r, vec3 c)
{
    int x = 0;
    int y = r;
    double e = 5.0/4.0 - r;
    draw_circle_parts(vec2(x, y), center, c);
    while (y > x) {
        if (e < 0) {
            e += 2 * x + 3;
        } else {
            e += 2 * x - 2 * y + 5;
            y -= 1;
        }
        x += 1;
        draw_circle_parts(vec2(x, y), center, c);
    }
}

void Window::draw_circle_parts(vec2 p, vec2 center, vec3 c)
{
    vec2 p1(p.x + center.x, p.y + center.y);
    vec2 p2(p.x + center.x, -(p.y - center.y));
    vec2 p3(-(p.x - center.x), p.y + center.y);
    vec2 p4(-(p.x - center.x), -(p.y - center.y));
    vec2 p5(p.y + center.y, p.x + center.x);
    vec2 p6(p.y + center.y, -(p.x - center.x));
    vec2 p7(-(p.y - center.y), p.x + center.x);
    vec2 p8(-(p.y - center.y), -(p.x - center.x));

    draw_pixel(p1,c);
    draw_pixel(p2,c);
    draw_pixel(p3,c);
    draw_pixel(p4,c);
    draw_pixel(p5,c);
    draw_pixel(p6,c);
    draw_pixel(p7,c);
    draw_pixel(p8,c);
}
//

void Window::raster_buffer_insert(int x, int raster_buffer[2])
{
	// TODO => TP03
}

void Window::raster_buffer_insert(int x, int raster_buffer[2], vec3 color, vec3 color_buffer[2])
{
	// TODO => TP04
}

void Window::draw_horizontal_line(int y, int x1, int x2, vec3 c)
{
	// TODO => TP03
}

void Window::draw_horizontal_line(int y, int x1, int x2, vec3 c[2])
{
	// TODO => TP04
}

void Window::draw_quad(vec2 p[4], vec3 c)
{
	// TODO => TP03
}

void Window::draw_quad(vec2 p[4], vec3 c[4])
{
	// TODO => TP04
}

unsigned char Window::f2c(float f)
{
	return (unsigned char)(f * 255);
}

float Window::c2f(unsigned char c)
{
	return ((float)c / 255.0f);
}
