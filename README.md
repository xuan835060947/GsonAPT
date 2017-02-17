# GsonAPT 
It is base in Gson lib and use Annotation Processor.If you can add the @JsonBean annotation to bean,the json parse will be faster;

it can also process the bean without @JsonBean,which will be processed by default Gson.In addition you can use GsonAPT.setGson()to change the default Gson in GsonAPT.

## How to use
### step 1 

dependencies {

    ...
    compile project(':gsonaptannotation')
    
    apt project(':gsonaptcomplier')
    
}


### step 2: add the annotation to bean
@JsonBean
public class LittleBean {
    String littleBeanName;

    public String getLittleBeanName() {
        return littleBeanName;
    }

    public void setLittleBeanName(String littleBeanName) {
        this.littleBeanName = littleBeanName;
    }

    public LittleBean() {
    }

    public LittleBean(String littleBeanName) {
        this.littleBeanName = littleBeanName;
    }
}

### step 3:compile
compile will generate the GsonAPT class
### step 4:use GsonAPT to replace new Gson()
        TestBean testBean = new TestBean();
        String str = GsonAPT.toJson(testBean);
        testBean = GsonAPT.fromJson(str,TestBean.class);

        Map<Integer, OtherBean> map = new HashMap<>();
        map.put(9, new OtherBean(""));
        map.put(1, new OtherBean(null));
        String mapStr = GsonAPT.toJson(map);
        map = GsonAPT.fromJson(mapStr,new TypeToken<Map<Integer, OtherBean>>() {
        }.getType());
        
        
## How fast
how much time to parse the small bean:

 times | Gson toJson | GsonAPT toJson | Gson fromJson | GsonAPT fromJson
 ------|-------------|----------------|---------------|-----------------
 10    | 3           |   1            | 10            | 1  
 10000 | 738         | 363 | 883 | 756 
 100000 | 8361 | 4420 | 10616 | 9346 

how much time to parse the big bean:

 times | Gson toJson | GsonAPT toJson | Gson fromJson | GsonAPT fromJson
 ------|-------------|----------------|---------------|-----------------
 1 | 673 | 325 | 811 | 700
 10 | 7748 | 3740 | 9506 | 8182
  
 
 times |Gson toJson Speed / GsonAPT toJson Speed
 ------|-------------
 10000(small) | 49%       
 100000(small) | 52% 
 1(big) | 48% 
 10(big) | 48% 
 
 
 times |Gson fromJson Speed / GsonAPT fromJson Speed
 ------|-------------
 10000(small) | 86%       
 100000(small) | 88% 
 1(big) | 86% 
 10(big) | 86%    
## attention
1. the field should was public , default or have the get/set method in the bean 
2. it no support the non-static inner class like the default Gson


